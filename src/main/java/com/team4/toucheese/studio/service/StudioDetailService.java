package com.team4.toucheese.studio.service;

import com.team4.toucheese.review.dto.ReviewDto;
import com.team4.toucheese.review.service.ReviewService;
import com.team4.toucheese.studio.dto.*;
import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.entity.Portfolio;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.entity.StudioOpeningHours;
import com.team4.toucheese.studio.repository.MenuRepository;
import com.team4.toucheese.studio.repository.PortfolioRepository;
import com.team4.toucheese.studio.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudioDetailService {
    private final StudioRepository studioRepository;
    private final MenuRepository menuRepository;
    private final ReviewService reviewService;
    private final PortfolioService portfolioService;

    //스튜디오 하나 보여주기
    public StudioDetailDto selectOneStudio(long studioId){
        Studio studio = studioRepository.findById(studioId).orElse(null);
        if (studio != null) {
            StudioDetailDto studioDetailDto = StudioDetailDto.fromEntity(studio);

            //영업중임을 확인하기위한 시간
            LocalDate nowDate = LocalDate.now();
            LocalTime nowTime = LocalTime.now();

            //영업중인지 확인
            studioDetailDto.setOpen(isStudioOpen(studioId, nowDate, nowTime));


            return studioDetailDto;

        }else
            return null;
    }

    //스튜디오의 메뉴 보여주기
    public List<MenuDetailDto> findStudioMenu(long studioId){
        List<Menu> menus = menuRepository.findByStudioId(studioId);
//        System.out.println("menus = " + menus);


        //데이터 DTO로 반환 후 return
        return menus.stream().map(menu -> {
            MenuDetailDto menuDetailDto = MenuDetailDto.fromEntity(menu);
            menuDetailDto.setReviewCount(reviewService.countReviewNum(menu.getId()));
            return menuDetailDto;
        }).toList();
    }

    //스튜디오의 메뉴 하나 보여주기
    public MenuDetailDto findMenu(long menuId, Pageable pageable){
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        MenuDetailDto menuDetailDto = new MenuDetailDto();
        Page<ReviewDto> reviewDtos = reviewService.findMenuReview(menuId, pageable);
        //리뷰 평점
        List<ReviewDto> allReviews = reviewService.findByMenuId(menuId);

        int totalReviews = allReviews.size();
        int totalScore = allReviews.stream()
                .filter(Objects::nonNull)
                .mapToInt(ReviewDto::getRating)
                .sum();
        double avgScore = (totalReviews > 0) ? ((double)totalScore / totalReviews) : 0.0;

        //DTO로 변환
        menuDetailDto = MenuDetailDto.fromEntity(menu);
        menuDetailDto.setReviewCount(reviewService.countReviewNum(menu.getId()));
        menuDetailDto.setReviews(reviewDtos);
        menuDetailDto.setAvgScore(avgScore);

        return menuDetailDto;
    }

    //스튜디오 포트폴리오 모아서 보여주기
    public PortfolioWithMenu findStudioPortfolio(long studioId, Pageable pageable, Long menuId){
        //DTO 생성
        PortfolioWithMenu portfolioWithMenu = new PortfolioWithMenu();

        //Page<PortfolioDto> 가져오기
        Page<PortfolioDto> portfolios = portfolioService.findStudioPortfolio(studioId, pageable);
        //DTO에 set
        portfolioWithMenu.setPortfolioDtos(portfolios);

        //스튜디오의 메뉴이름 및 아이디 가져오기
        List<Menu> menus = menuRepository.findByStudioId(studioId);
        List<Long> menuIds = menus.stream().map(Menu::getId).toList();
        //DTO에 set
        portfolioWithMenu.setMenuIdList(menuIds);
        portfolioWithMenu.setMenuNameList(menus.stream().map(Menu::getName).toList());

        //menuId에 따른 필터링
        if (menuId != null){
            //Page<> -> List<>로 변환
            List<PortfolioDto> filteredList = portfolioWithMenu.getPortfolioDtos().getContent().stream()
                    .filter(portfolioDto -> portfolioDto.getMenuId().equals(menuId))
                    .toList();
            //다시 Page형태로 변환
            portfolioWithMenu.setPortfolioDtos(
                    new PageImpl<>(filteredList)
            );
        }

        return portfolioWithMenu;

    }

    //스튜디오 오픈 여부
    public boolean isStudioOpen(Long studioId, LocalDate date, LocalTime time){
        Studio studio = studioRepository.findById(studioId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //특별 휴무 확인
        boolean isSpecialHoliday = studio.getSpecialHolidays().stream()
                .anyMatch(studioSpecialHoliday -> studioSpecialHoliday.getDate().equals(date));
        if (isSpecialHoliday) return false;

        //특정 주의 요일 휴무 확인
        //몇주차 계산
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        //특정 월의 몇 번째 주인지
        int weekOfMonth = date.get(weekFields.weekOfMonth());
        //휴무확인
        boolean isHoliday = studio.getHolidays().stream()
                .anyMatch(studioHoliday -> studioHoliday.getWeekOfMonth() == weekOfMonth
                && studioHoliday.getDayOfWeek() == date.getDayOfWeek());
        if (isHoliday) return false;

        //요일별 휴무 확인
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        StudioOpeningHours openingHours = studio.getOpeningHours().stream()
                .filter(studioOpeningHours -> studioOpeningHours.getDayOfWeek() == dayOfWeek)
                .findFirst().orElse(null);
        if (openingHours == null || openingHours.isClosed()) return false;


        //시간 확인
        return time.isAfter(openingHours.getOpenTime().toLocalTime())
                && time.isBefore(openingHours.getCloseTime().toLocalTime());

    }

}

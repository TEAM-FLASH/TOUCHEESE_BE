package com.team4.toucheese.studio.service;

import com.team4.toucheese.review.service.ReviewService;
import com.team4.toucheese.studio.dto.MenuDetailDto;
import com.team4.toucheese.studio.dto.PortfolioDto;
import com.team4.toucheese.studio.dto.StudioDto;
import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.entity.Portfolio;
import com.team4.toucheese.studio.entity.Studio;
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

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudioDetailService {
    private final StudioRepository studioRepository;
    private final MenuRepository menuRepository;
    private final ReviewService reviewService;
    private final PortfolioRepository portfolioRepository;

    //스튜디오 하나 보여주기
    public StudioDto selectOneStudio(long studioId){
        Studio studio = studioRepository.findById(studioId).orElse(null);
        if (studio != null) {
            return StudioDto.fromEntity(studio);
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
    public MenuDetailDto findMenu(long MenuId){
        Menu menu = menuRepository.findById(MenuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return MenuDetailDto.fromEntity(menu);
    }

    //스튜디오 포트폴리오 모아서 보여주기
    public Page<PortfolioDto> findStudioPortfolio(long studioId, Pageable pageable){
        List<Portfolio> portfolios = portfolioRepository.findByStudioId(studioId);

        //DTO로 변환
        List<PortfolioDto> portfolioDtos = portfolios.stream().map(PortfolioDto::fromEntity).toList();

        //페이징
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), portfolioDtos.size());
        List<PortfolioDto> pagedPortfolioDtos = portfolioDtos.subList(start, end);

        return new PageImpl<>(pagedPortfolioDtos, pageable, portfolioDtos.size());

    }

}

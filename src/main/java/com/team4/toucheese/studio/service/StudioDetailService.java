package com.team4.toucheese.studio.service;

import com.team4.toucheese.review.dto.ReviewDetailWithTotal;
import com.team4.toucheese.review.dto.ReviewDto;
import com.team4.toucheese.review.entity.Review;
import com.team4.toucheese.review.repository.ReviewRepository;
import com.team4.toucheese.review.service.ReviewService;
import com.team4.toucheese.studio.dto.MenuDetailDto;
import com.team4.toucheese.studio.dto.StudioDto;
import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.repository.MenuRepository;
import com.team4.toucheese.studio.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudioDetailService {
    private final StudioRepository studioRepository;
    private final MenuRepository menuRepository;
    private final ReviewService reviewService;

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
        return menus.stream().map(MenuDetailDto::fromEntity).toList();
    }

}

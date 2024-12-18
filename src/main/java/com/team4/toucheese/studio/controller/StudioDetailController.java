package com.team4.toucheese.studio.controller;

import com.team4.toucheese.review.dto.ReviewDetailWithTotal;
import com.team4.toucheese.review.dto.ReviewDto;
import com.team4.toucheese.review.dto.ReviewImageDto;
import com.team4.toucheese.review.service.ReviewImageService;
import com.team4.toucheese.review.service.ReviewService;
import com.team4.toucheese.studio.dto.MenuDetailDto;
import com.team4.toucheese.studio.dto.PortfolioDto;
import com.team4.toucheese.studio.dto.StudioDto;
import com.team4.toucheese.studio.entity.Portfolio;
import com.team4.toucheese.studio.service.StudioDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studio/detail")
@RequiredArgsConstructor
public class StudioDetailController {
    private final StudioDetailService studioDetailService;
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    @GetMapping("/{studioId}")
    public ResponseEntity<StudioDto> selectOne(@PathVariable("studioId") long StudioId) {
        try {
            return ResponseEntity.ok(studioDetailService.selectOneStudio(StudioId));
        }catch ( ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch ( Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{studioId}/menu")
    public ResponseEntity<List<MenuDetailDto>> studioMenu(@PathVariable("studioId") long studioId){
        try{
            return ResponseEntity.ok(studioDetailService.findStudioMenu(studioId));
        }catch ( ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch ( Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/menu/{menuId}")
    public ResponseEntity<MenuDetailDto> findMenu(@PathVariable("menuId") long menuId){
        try {
            return ResponseEntity.ok(studioDetailService.findMenu(menuId));
        }catch ( ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch ( Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{studioId}/reviews")
    public ResponseEntity<ReviewDetailWithTotal> studioReview(@PathVariable("studioId") long studioId, Pageable pageable){
        try {
            return ResponseEntity.ok(reviewService.findReviewWithTotal(studioId, pageable));
        }catch ( ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch ( Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{studioId}/reviewImage")
    public ResponseEntity<Page<ReviewImageDto>> studioReviewImage(@PathVariable("studioId") long studioId, Pageable pageable){
        try{
            return ResponseEntity.ok(reviewImageService.findAllReviewImage(studioId, pageable));
        }catch ( ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch ( Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{studioId}/portfolio")
    public ResponseEntity<Page<PortfolioDto>> studioPortfolio(@PathVariable("studioId") long studioId, Pageable pageable){
        try {
            return ResponseEntity.ok(studioDetailService.findStudioPortfolio(studioId, pageable));
        }catch ( ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch ( Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}

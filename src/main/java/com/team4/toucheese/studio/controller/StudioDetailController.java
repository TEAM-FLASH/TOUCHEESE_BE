package com.team4.toucheese.studio.controller;

import com.team4.toucheese.review.dto.ReviewDto;
import com.team4.toucheese.studio.dto.MenuDetailDto;
import com.team4.toucheese.studio.dto.StudioDto;
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

    @GetMapping("/{studioId}/reviews")
    public ResponseEntity<Page<ReviewDto>> studioReview(@PathVariable("studioId") long studioId, Pageable pageable){
        try {
            return ResponseEntity.ok(studioDetailService.findStudioReview(studioId, pageable));
        }catch ( ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch ( Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}

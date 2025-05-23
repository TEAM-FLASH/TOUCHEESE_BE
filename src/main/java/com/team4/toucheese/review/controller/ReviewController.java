package com.team4.toucheese.review.controller;

import com.team4.toucheese.auth.dto.CustomUserDetails;
import com.team4.toucheese.review.dto.CreateReviewRequestDto;
import com.team4.toucheese.review.dto.ErrorDto;
import com.team4.toucheese.review.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final S3Service s3Service;

    @PostMapping(value = "/file", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(
            @RequestPart
            List<MultipartFile> multipartFiles,
            @RequestPart
            String content,
            @RequestPart
            Long menuId,
            @RequestPart
            Integer rating,
            @RequestPart
            Long studioId,
            @RequestPart
            List<Long> additionalOptionIds,
            @RequestPart
            Long reservationId,
            Authentication authentication
    ) {
        try{
            CreateReviewRequestDto createReviewRequestDto = new CreateReviewRequestDto();
            createReviewRequestDto.setMultipartFiles(multipartFiles);
            createReviewRequestDto.setContent(content);
            createReviewRequestDto.setMenuId(menuId);
            createReviewRequestDto.setRating(rating);
            createReviewRequestDto.setStudioId(studioId);
            createReviewRequestDto.setAdditionalOptionIds(additionalOptionIds);
            createReviewRequestDto.setReservationId(reservationId);
            return ResponseEntity.ok(s3Service.uploadReview(createReviewRequestDto, authentication));
        }catch (ConfigDataResourceNotFoundException e){
            ErrorDto errorDto = new ErrorDto();
            errorDto.setMessage(e.getMessage());
            System.out.println("e = " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
        }catch (IllegalArgumentException e){
            ErrorDto errorDto = new ErrorDto();
            errorDto.setMessage(e.getMessage());
            System.out.println("e = " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
        }catch (Exception e){
            ErrorDto errorDto = new ErrorDto();
            errorDto.setMessage(e.getMessage());
            System.out.println("e = " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
        }
    }
}

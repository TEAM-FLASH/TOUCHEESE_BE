package com.team4.toucheese.review.controller;

import com.team4.toucheese.auth.dto.CustomUserDetails;
import com.team4.toucheese.review.dto.CreateReviewRequestDto;
import com.team4.toucheese.review.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final S3Service s3Service;

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestBody CreateReviewRequestDto createReviewRequestDto, Authentication authentication) {
        try{
            return ResponseEntity.ok(s3Service.uploadReview(createReviewRequestDto, authentication));
        }catch (ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

package com.team4.toucheese.review.service;

import com.team4.toucheese.review.dto.ReviewDto;
import com.team4.toucheese.review.entity.Review;
import com.team4.toucheese.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<ReviewDto> findStudioReview(Long studioId){
        if (studioId == null){
            throw new IllegalArgumentException("studioId is null");
        }

        //스튜디오의 리뷰 찾기
        List<Review> reviews = reviewRepository.findByMenu_Studio_Id(studioId);

        if (reviews.isEmpty()){
            return List.of();
        }

        //데이터 DTO로 변환후 return
        return reviews.stream().map(ReviewDto::fromEntity).toList();
    }
}

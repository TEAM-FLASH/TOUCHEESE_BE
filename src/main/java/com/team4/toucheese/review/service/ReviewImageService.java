package com.team4.toucheese.review.service;

import com.team4.toucheese.review.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewImageService {
    private final ReviewImageRepository reviewImageRepository;

    public Integer countReviewImageNum(Long reviewId){
        if (reviewId == null){
            return -1;
        }
        return reviewImageRepository.findByReview_Id(reviewId).size();
    }

    public boolean existReviewImage(Long reviewId){
        return reviewImageRepository.existsByReview_Id( reviewId);
    }
}

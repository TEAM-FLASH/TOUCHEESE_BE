package com.team4.toucheese.review.service;

import com.team4.toucheese.review.dto.ReviewDetailWithTotal;
import com.team4.toucheese.review.dto.ReviewDto;
import com.team4.toucheese.review.entity.Review;
import com.team4.toucheese.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageService reviewImageService;

    public List<ReviewDto> findStudioReview(Long studioId){
        if (studioId == null){
            throw new IllegalArgumentException("studioId is null");
        }

        //스튜디오의 리뷰 찾기
        List<Review> reviews = reviewRepository.findByMenu_Studio_Id(studioId);

        if (reviews.isEmpty()){
            return List.of();
        }
        //데이터 DTO로 변환
        List<ReviewDto> reviewDtos = reviews.stream().map(review -> {
            ReviewDto reviewDto = ReviewDto.fromEntity(review);

            //이미지가 존재하는지
            if (reviewImageService.existReviewImage(review.getId())){
                reviewDto.setImageExists(true);
            }
            return reviewDto;
        }).toList();

        return reviewDtos;
    }

    public ReviewDetailWithTotal findReviewWithTotal(Long studioId, Pageable pageable){
        ReviewDetailWithTotal reviewDetailWithTotal = new ReviewDetailWithTotal();
        List<ReviewDto> reviewDtos = findStudioReview(studioId);
        int totalSize = reviewDtos.size();
        //페이징
        //시작
        int start = (int) pageable.getOffset();
        //끝
        int end = Math.min((start + pageable.getPageSize()), totalSize);
        List<ReviewDto> pagedReviews = reviewDtos.subList(start, end);

        int totalRating = 0;
        int totalImageNum = 0;
        for (ReviewDto reviewDto : reviewDtos){
            totalRating += reviewDto.getRating();
            totalImageNum += reviewDto.getReviewImages().size();
        }
        double avgRating = (double) totalRating / totalSize;

        reviewDetailWithTotal.setReviewList(pagedReviews);
        reviewDetailWithTotal.setTotalImageNum(totalImageNum);
        reviewDetailWithTotal.setAvgRating(avgRating);
        reviewDetailWithTotal.setTotalReviewNum(totalSize);

        return reviewDetailWithTotal;

    }

    public Integer countReviewNum(Long menuId){
        return reviewRepository.findByMenu_Id(menuId).size();
    }

}

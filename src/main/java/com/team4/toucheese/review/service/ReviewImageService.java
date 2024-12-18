package com.team4.toucheese.review.service;

import com.team4.toucheese.review.dto.ReviewImageDto;
import com.team4.toucheese.review.entity.ReviewImage;
import com.team4.toucheese.review.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ReviewImage> findByStudio(Long studioId){
        return reviewImageRepository.findTop4ByReview_Menu_Studio_IdOrderByIdDesc(studioId);
    }

    //리뷰사진 모아보기
    public Page<ReviewImageDto> findAllReviewImage(Long studioId, Pageable pageable){
        List<ReviewImage> reviewImages = reviewImageRepository.findByReview_Menu_Studio_Id(studioId);
        //DTO 변환
        List<ReviewImageDto> reviewImageDtos = reviewImages.stream().map(ReviewImageDto::fromEntity).toList();

        //페이징
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewImageDtos.size());
        List<ReviewImageDto> pagedReviewImageDtos = reviewImageDtos.subList(start, end);

        return new PageImpl<>(pagedReviewImageDtos, pageable, reviewImageDtos.size());
    }

}

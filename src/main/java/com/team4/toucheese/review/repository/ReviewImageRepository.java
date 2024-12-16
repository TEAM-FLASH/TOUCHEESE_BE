package com.team4.toucheese.review.repository;

import com.team4.toucheese.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findByReview_Id(Long reviewId);
    Boolean existsByReview_Id(Long reviewId);
}

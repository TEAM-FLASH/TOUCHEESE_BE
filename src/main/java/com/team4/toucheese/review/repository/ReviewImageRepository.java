package com.team4.toucheese.review.repository;

import com.team4.toucheese.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findByReview_Id(Long reviewId);
    Boolean existsByReview_Id(Long reviewId);

    List<ReviewImage> findTop4ByReview_Menu_Studio_IdOrderByIdDesc(Long reviewMenuStudioId);

    List<ReviewImage> findByReview_Menu_Studio_Id(Long reviewMenuStudioId);
}

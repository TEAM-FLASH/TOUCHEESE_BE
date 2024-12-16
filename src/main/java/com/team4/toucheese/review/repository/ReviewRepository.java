package com.team4.toucheese.review.repository;

import com.team4.toucheese.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMenu_Studio_Id(Long menuStudioId);

    List<Review> findByMenu_Id(Long menuId);

//    List<Review> findByMenu_Studio_Id(Long menuStudioId);

}

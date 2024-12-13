package com.team4.toucheese.review.dto;

import com.team4.toucheese.review.entity.Review;
import com.team4.toucheese.review.entity.ReviewImage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageDto {
    private Long id;
    private Long reviewId;
    private String url;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static ReviewImageDto fromEntity(ReviewImage entity){
        return ReviewImageDto.builder()
                .id(entity.getId())
                .reviewId(entity.getReview().getId())
                .url(entity.getUrl())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }
}

package com.team4.toucheese.review.dto;

import com.team4.toucheese.review.entity.ReviewImage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageDetailDto {
    private Long id;
    private Long reviewId;
    private String url;
    private String reviewContent;
    private String menuName;
    private Integer rating;
    private LocalDateTime writeTime;
    private String userName;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static ReviewImageDetailDto fromEntity(ReviewImage entity){
        return ReviewImageDetailDto.builder()
                .id(entity.getId())
                .reviewId(entity.getReview().getId())
                .url(entity.getUrl())
                .reviewContent(entity.getReview().getContent())
                .menuName(entity.getReview().getMenu().getName())
                .rating(entity.getReview().getRating())
                .writeTime(entity.getReview().getCreated_at())
                .userName(entity.getReview().getUser().getUsername())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }
}

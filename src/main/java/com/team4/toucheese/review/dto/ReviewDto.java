package com.team4.toucheese.review.dto;

import com.team4.toucheese.review.entity.Review;
import com.team4.toucheese.review.entity.ReviewImage;
import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.user.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long menuId;
    private String menuName;
    private String content;
    private Integer rating;
    private List<ReviewImageDto> reviewImages;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private boolean imageExists;

    public static ReviewDto fromEntity(Review entity){
        return ReviewDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .userName(entity.getUser().getUsername())
                .menuId(entity.getMenu().getId())
                .menuName(entity.getMenu().getName())
                .content(entity.getContent())
                .rating(entity.getRating())
                .reviewImages(entity.getReviewImages().stream().map(ReviewImageDto::fromEntity).toList())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }
}

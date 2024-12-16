package com.team4.toucheese.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailWithTotal {
    private List<ReviewDto> reviewList;
    private Double avgRating;
    private Integer totalImageNum;
    private Integer totalReviewNum;
}

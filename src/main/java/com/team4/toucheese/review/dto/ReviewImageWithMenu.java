package com.team4.toucheese.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageWithMenu {
    private List<ReviewImageDto> imageDtos;
    private List<String> menuNameList;
    private List<Long> menuIdList;
    private Integer totalPages;
    private Integer currentPage;
    private Long totalElements;
}

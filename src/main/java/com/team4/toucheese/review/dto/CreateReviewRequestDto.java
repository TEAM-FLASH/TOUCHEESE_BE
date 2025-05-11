package com.team4.toucheese.review.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {
    private List<MultipartFile> multipartFiles;
    private String content;
    private Long menuId;
    private Integer rating;
    private Long studioId;
    private List<Long> additionalOptionIds;
    private Long reservationId;
}

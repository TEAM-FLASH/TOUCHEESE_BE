package com.team4.toucheese.user.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCompletedInfo {
    private Long reservationId;
    private Long studioId;
    private String studioName;
    private Long menuId;
    private String menuName;
    private List<Long> additionalOptionIds;
    private List<String> additionalOptionNames;
    private LocalDate date;
    private LocalTime startTime;
    private String menuImgUrl;
    private String status;

    private boolean review;
    private Integer reviewRating;
}

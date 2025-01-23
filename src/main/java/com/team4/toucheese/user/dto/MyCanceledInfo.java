package com.team4.toucheese.user.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCanceledInfo {
    private Long completedReservationId;
    private Long studioId;
    private String studioName;
    private Long menuId;
    private String menuName;
    private LocalDate date;
    private LocalTime startTime;
    private String menuImgUrl;
    private String status;
    private String cancelReason;
    private String cancelReasonDetail;
}

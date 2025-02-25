package com.team4.toucheese.studio.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResultDto {
    private Long reservationId;
    private Long studioId;
    private String studioName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long menuId;
    private String menuName;
    private List<Long> additionalMenuIds;
    private List<String> additionalMenuNames;
    private List<Integer> additionalMenuPrices;
    private String userName;
    private String userPhone;
    private String note;
    private Long totalPrice;
    private String status;
}

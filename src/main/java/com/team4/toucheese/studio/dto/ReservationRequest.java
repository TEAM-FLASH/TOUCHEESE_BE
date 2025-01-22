package com.team4.toucheese.studio.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private Long studioId;
    private Long menuId;
    private List<Long> additionalOptionIds;
    private Long userId;
    private String visitingCustomerName;
    private String visitingCustomerPhone;
    private String note;
    private Long totalPrice;
    private LocalDate date;
    private LocalTime startTime;
    private String paymentMethod;
    private String impUid;
    private String merchantUid;

}

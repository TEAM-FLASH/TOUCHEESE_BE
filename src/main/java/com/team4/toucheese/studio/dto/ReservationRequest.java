package com.team4.toucheese.studio.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private Long studioId;
    private Long menuId;
    private String time;
}

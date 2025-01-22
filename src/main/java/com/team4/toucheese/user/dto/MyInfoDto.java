package com.team4.toucheese.user.dto;

import com.team4.toucheese.studio.entity.Reservation;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoDto {
    private Long studioId;
    private String studioName;
    private Long menuId;
    private String menuName;
    private LocalDate date;
    private LocalTime startTime;
    private String menuImgUrl;
    private String status;
}

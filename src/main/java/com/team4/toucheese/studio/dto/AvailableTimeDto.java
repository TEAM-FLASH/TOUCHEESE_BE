package com.team4.toucheese.studio.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeDto {
    private String time;
    private boolean isAvailable;
}

package com.team4.toucheese.studio.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeWithDateDto {
    private String date;
    private List<AvailableTimeDto> availableTimeDto;
}

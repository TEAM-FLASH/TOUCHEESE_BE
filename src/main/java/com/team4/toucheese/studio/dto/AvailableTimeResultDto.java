package com.team4.toucheese.studio.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeResultDto {
    List<AvailableTimeWithDateDto> availableTimeWithDates;
    List<String> disableDates;
}

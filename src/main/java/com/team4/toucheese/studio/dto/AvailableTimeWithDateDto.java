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
    private AvailableTimeDto availableTimeDto;
    private List<String> disableDateList;
}

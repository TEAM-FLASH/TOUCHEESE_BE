package com.team4.toucheese.studio.dto;

import com.team4.toucheese.studio.entity.StudioSpecialHoliday;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudioSpecialHolidayDto {

    private Long id;
    private Long studioId;
    private String studioName;
    private LocalDate date;  //특정 휴무일
    private String reason;  //휴무 이유

    public static StudioSpecialHolidayDto fromEntity(StudioSpecialHoliday entity) {
        return StudioSpecialHolidayDto.builder()
                .id(entity.getId())
                .studioId(entity.getStudio().getId())
                .studioName(entity.getStudio().getName())
                .date(entity.getDate())
                .reason(entity.getReason())
                .build();
    }

}

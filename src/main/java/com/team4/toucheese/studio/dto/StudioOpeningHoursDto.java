package com.team4.toucheese.studio.dto;

import com.team4.toucheese.studio.entity.StudioOpeningHours;
import lombok.*;

import java.sql.Time;
import java.time.DayOfWeek;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudioOpeningHoursDto {

    private Long id;
    private Long studioId;
    private String studioName;
    private DayOfWeek dayOfWeek;
    private Time openTime;
    private Time closeTime;

    private boolean isClosed; // 해당 요일이 휴무인지

    public static StudioOpeningHoursDto fromEntity(StudioOpeningHours entity) {
        return StudioOpeningHoursDto.builder()
                .id(entity.getId())
                .studioId(entity.getStudio().getId())
                .studioName(entity.getStudio().getName())
                .dayOfWeek(entity.getDayOfWeek())
                .openTime(entity.getOpenTime())
                .closeTime(entity.getCloseTime())
                .isClosed(entity.isClosed())
                .build();
    }
}

package com.team4.toucheese.studio.dto;

import com.team4.toucheese.studio.entity.StudioHoliday;
import lombok.*;

import java.time.DayOfWeek;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudioHolidayDto {

    private Long id;
    private Long studioId;
    private String studioName;
    private int weekOfMonth;
    private DayOfWeek dayOfWeek;

    public static StudioHolidayDto fromEntity(StudioHoliday entity) {
        return StudioHolidayDto.builder()
                .id(entity.getId())
                .studioId(entity.getStudio().getId())
                .studioName(entity.getStudio().getName())
                .weekOfMonth(entity.getWeekOfMonth())
                .dayOfWeek(entity.getDayOfWeek())
                .build();
    }
}

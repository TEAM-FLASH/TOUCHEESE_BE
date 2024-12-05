package com.team4.toucheese.studio.dto;

import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.entity.StudioOption;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudioOptionDto {
    private Long id;
    private StudioOption.OptionName name;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static StudioOptionDto fromEntity(StudioOption entity){
        return StudioOptionDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }
}

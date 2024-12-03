package com.team4.toucheese.studio.dto;

import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.entity.Studio;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private Long id;
    private String studio;
    private String name;
    private String description;
    private Integer price;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static MenuDto fromEntity(Menu entity){
        return MenuDto.builder()
                .id(entity.getId())
                .studio(entity.getStudio().getName())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }
}

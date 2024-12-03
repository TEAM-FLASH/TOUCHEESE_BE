package com.team4.toucheese.studio.dto;

import com.team4.toucheese.studio.entity.Portfolio;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.vibe.entity.Vibe;
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
public class PortfolioDto {

    private Long id;
    private String studio;
    private String vibe;
    private String name;
    private String url;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static PortfolioDto fromEntity(Portfolio entity){
        return PortfolioDto.builder()
                .id(entity.getId())
                .studio(entity.getStudio().getName())
                .vibe(entity.getVibe().getName())
                .name(entity.getName())
                .url(entity.getUrl())
                .description(entity.getDescription())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }
}

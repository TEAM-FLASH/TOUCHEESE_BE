package com.team4.toucheese.studio.dto;

import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.entity.MenuImage;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
public class MenuImageDto {
    private Long id;
    private Long menuId;
    private String menu;
    private String url;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static MenuImageDto fromEntity(MenuImage entity){
        return MenuImageDto.builder()
                .id(entity.getId())
                .menuId(entity.getMenu().getId())
                .menu(entity.getMenu().getName())
                .url(entity.getUrl())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }
}

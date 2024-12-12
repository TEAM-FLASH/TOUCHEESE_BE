package com.team4.toucheese.studio.dto;

import com.team4.toucheese.studio.entity.AdditionalOption;
import com.team4.toucheese.studio.entity.Menu;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalOptionDto {
    private Long id;
    private Long menuId;
    private String menu;
    private String name;
    private Integer price;
    private String description;
    private Time duration;  //촬영시간
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static AdditionalOptionDto fromEntity(AdditionalOption entity){
        return AdditionalOptionDto.builder()
                .id(entity.getId())
                .menuId(entity.getMenu().getId())
                .menu(entity.getMenu().getName())
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .duration(entity.getDuration())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
}

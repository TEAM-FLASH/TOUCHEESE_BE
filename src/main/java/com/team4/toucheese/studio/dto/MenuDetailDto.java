package com.team4.toucheese.studio.dto;

import com.team4.toucheese.review.dto.ReviewDto;
import com.team4.toucheese.studio.entity.AdditionalOption;
import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.entity.MenuImage;
import com.team4.toucheese.studio.entity.Studio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Page;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDetailDto {
    private Long id;
    private Long studioId;
    private String studioName;
    private String name;
    private String description;
    private Integer price;
    private Time duration;  //촬영시간
    private List<AdditionalOptionDto> additionalOptions;
    private List<MenuImageDto> menuImages;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private Page<ReviewDto> reviews;
    double avgScore;

    private Integer reviewCount;

    public static MenuDetailDto fromEntity(Menu entity){
        return MenuDetailDto.builder()
                .id(entity.getId())
                .studioId(entity.getId())
                .studioName(entity.getStudio().getName())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .duration(entity.getDuration())
                .additionalOptions(entity.getAdditionalOptions().stream().map(AdditionalOptionDto::fromEntity).toList())
                .menuImages(entity.getMenuImages().stream().map(MenuImageDto::fromEntity).toList())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }

}

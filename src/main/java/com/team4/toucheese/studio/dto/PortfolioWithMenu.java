package com.team4.toucheese.studio.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioWithMenu {
    private Page<PortfolioDto> portfolioDtos;

    private List<Long> menuIdList;
    private List<String> menuNameList;

    private String studioName;

}

package com.team4.toucheese.studio.dto;

import com.team4.toucheese.address.entity.Address_Gu;
import com.team4.toucheese.address.entity.Address_Si;
import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.entity.Portfolio;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.vibe.entity.Vibe;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudioDto {

    private Long id;
    private String vibe;  //분위기
    private String addressSi;
    private String addressGu;
    private String name;    //스튜디오 이름
    private String description; //스튜디오 설명
    private String address; //스튜디오 주소
    private String phone;   //스튜디오 연락처
    private Long view_count;    //조회 수
    private Double rating;  //평점
    private Long bookmark_count;    //북마크 수
    private Long review_count;  //리뷰 수
    private Double latitude;    //위도
    private Double longitude;   //경도
    private Time open_time; //오픈시간
    private Time close_time;    //마감시간
    private String subVibe;   //서브분위기
    private List<PortfolioDto> portfolios;
    private List<MenuDto> menus;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private Studio.DayOfWeek day_of_week;  //휴무일

    public static StudioDto fromEntity(Studio entity){
        return StudioDto.builder()
                .id(entity.getId())
                .vibe(entity.getVibe().getName())
                .addressSi(entity.getAddressSi().getName())
                .addressGu(entity.getAddressGu().getName())
                .name(entity.getName())
                .description(entity.getDescription())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .view_count(entity.getView_count())
                .rating(entity.getRating())
                .bookmark_count(entity.getBookmark_count())
                .review_count(entity.getReview_count())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .open_time(entity.getOpen_time())
                .close_time(entity.getClose_time())
                .subVibe(entity.getSubVibe().getName())
                .portfolios(entity.getPortfolios().stream().map(PortfolioDto::fromEntity).toList())
                .menus(entity.getMenus().stream().map(MenuDto::fromEntity).toList())
                .day_of_week(entity.getDay_of_week())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }
}

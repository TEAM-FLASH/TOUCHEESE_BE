package com.team4.toucheese.studio.dto;

import com.team4.toucheese.studio.entity.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudioDetailDto {
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
    private String subVibe;   //서브분위기
    private boolean bookmarked;

    private List<PortfolioDto> portfolios;
    private List<String> options;

    private boolean isOpen;

    //스튜디오 휴무일 및 영업시간

    //요일별 영업시간
    private List<StudioOpeningHoursDto> openingHours;

    //특정 주의 요일 휴무
    private List<StudioHolidayDto> holidays;

    public static StudioDetailDto fromEntity(Studio entity){
        return StudioDetailDto.builder()
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
                .subVibe(entity.getSubVibe().getName())
                .portfolios(entity.getPortfolios().stream().map(PortfolioDto::fromEntity).toList())
                .options(entity.getOptions().stream().map(studioOption -> studioOption.getName().name()).toList())
                .openingHours(entity.getOpeningHours().stream().map(StudioOpeningHoursDto::fromEntity).toList())
                .holidays(entity.getHolidays().stream().map(StudioHolidayDto::fromEntity).toList())
                .build();
    }
}

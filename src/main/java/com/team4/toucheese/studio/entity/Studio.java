package com.team4.toucheese.studio.entity;

import com.team4.toucheese.address.entity.Address_Gu;
import com.team4.toucheese.address.entity.Address_Si;
import com.team4.toucheese.vibe.entity.Vibe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Studio {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Vibe vibe;  //분위기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Address_Si addressSi;
    @ManyToOne(fetch = FetchType.LAZY)
    private Address_Gu addressGu;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Vibe subVibe;   //서브분위기

    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Portfolio> portfolios;
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menus;
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Schedule> schedules;
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudioOption> options;
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    //스튜디오 휴무일 및 영업시간

    //요일별 영업시간
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<StudioOpeningHours> openingHours;

    //특정 주의 요일 휴무
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<StudioHoliday> holidays;

    //특정 날 휴무
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<StudioSpecialHoliday> specialHolidays;



    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day_of_week;  //휴무일

    public enum DayOfWeek {
        MON, TUE, WED, THU, FRI, SAT, SUN    }


}

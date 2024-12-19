package com.team4.toucheese.studio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.DayOfWeek;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudioOpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Studio studio;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private Time openTime;
    private Time closeTime;

    private boolean isClosed; // 해당 요일이 휴무인지


}

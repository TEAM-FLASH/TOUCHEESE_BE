package com.team4.toucheese.studio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudioHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Studio studio;

    private int weekOfMonth;    //몇 째 주

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

}

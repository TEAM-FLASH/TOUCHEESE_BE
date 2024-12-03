package com.team4.toucheese.studio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Studio studio;
    private Long user_id;
    private LocalDate date;
    private LocalTime start_time;
    private LocalTime end_time;
    private String note;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;

    public enum ReservationStatus {
        RESERVED,
        CANCELED,
        COMPLETED
    }
}

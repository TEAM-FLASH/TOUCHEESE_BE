package com.team4.toucheese.studio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<AdditionalOption> additionalOptions;

    private String visitingCustomerName;
    private String visitingCustomerPhone;
    private Long totalPrice;

    private String paymentMethod;
    private String impUid;
    private String merchantUid;


    public enum ReservationStatus {
        RESERVED,
        COMPLETED
    }
}

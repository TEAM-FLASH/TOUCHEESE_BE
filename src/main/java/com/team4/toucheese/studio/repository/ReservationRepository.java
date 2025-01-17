package com.team4.toucheese.studio.repository;

import com.team4.toucheese.studio.entity.Reservation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.studio.id = :studioId AND YEAR(r.date) = :year AND MONTH(r.date) = :month")
    List<Reservation> findReservationsForStudio(@Param("studioId") Long studioId,
                                                @Param("year") int year,
                                                @Param("month") int month);
}

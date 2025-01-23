package com.team4.toucheese.studio.repository;

import com.team4.toucheese.studio.entity.CancelReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CancelReservationRepository extends JpaRepository<CancelReservation, Long> {
    @Query("SELECT cr FROM CancelReservation cr WHERE cr.user_id = :userId")
    List<CancelReservation> findByUser(Long userId);
}

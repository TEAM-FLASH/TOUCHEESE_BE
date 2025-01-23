package com.team4.toucheese.studio.repository;

import com.team4.toucheese.studio.entity.CompleteReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompleteReservationRepository extends JpaRepository<CompleteReservation, Long> {
    @Query("SELECT cr FROM CompleteReservation cr WHERE cr.user_id = :userId")
    List<CompleteReservation> findByUser(Long userId);
}

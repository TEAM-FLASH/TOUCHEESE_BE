package com.team4.toucheese.studio.repository;

import com.team4.toucheese.studio.entity.StudioOpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudioOpeningHoursRepository extends JpaRepository<StudioOpeningHours, Long> {
    List<StudioOpeningHours> findByStudioId(Long studioId);
}

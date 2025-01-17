package com.team4.toucheese.studio.repository;

import com.team4.toucheese.studio.entity.StudioHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudioHolidayRepository extends JpaRepository<StudioHoliday, Long> {
    List<StudioHoliday> findByStudioId(Long studioId);
}

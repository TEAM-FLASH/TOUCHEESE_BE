package com.team4.toucheese.studio.repository;

import com.team4.toucheese.studio.entity.StudioSpecialHoliday;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudioSpecialHolidayRepository extends JpaRepository<StudioSpecialHoliday, Long> {
    @Query("SELECT s FROM StudioSpecialHoliday s WHERE s.studio.id = :studioId AND YEAR(s.date) = :year AND MONTH(s.date) = :month")
    List<StudioSpecialHoliday> findSpecialHolidaysForStudio(@Param("studioId") Long studioId,
                                                            @Param("year") int year,
                                                            @Param("month") int month);
}

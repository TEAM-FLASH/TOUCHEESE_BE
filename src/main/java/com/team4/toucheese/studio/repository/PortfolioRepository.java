package com.team4.toucheese.studio.repository;

import com.team4.toucheese.studio.dto.PortfolioDto;
import com.team4.toucheese.studio.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByStudioId(long studioId);
}

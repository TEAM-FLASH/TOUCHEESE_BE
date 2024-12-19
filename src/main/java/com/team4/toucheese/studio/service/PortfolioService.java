package com.team4.toucheese.studio.service;

import com.team4.toucheese.studio.dto.PortfolioDto;
import com.team4.toucheese.studio.entity.Portfolio;
import com.team4.toucheese.studio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    public Page<PortfolioDto> findStudioPortfolio(Long studioId, Pageable pageable){
        Page<Portfolio> portfolios = portfolioRepository.findByStudioId(studioId, pageable);
        return portfolios.map(PortfolioDto::fromEntity);
    }
}

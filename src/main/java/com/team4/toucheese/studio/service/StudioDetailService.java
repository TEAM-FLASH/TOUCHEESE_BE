package com.team4.toucheese.studio.service;

import com.team4.toucheese.studio.dto.StudioDto;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudioDetailService {
    private final StudioRepository studioRepository;

    public StudioDto selectOneStudio(long studioId){
        Studio studio = studioRepository.findById(studioId).orElse(null);
        if (studio != null) {
            return StudioDto.fromEntity(studio);
        }else
            return null;
    }


}

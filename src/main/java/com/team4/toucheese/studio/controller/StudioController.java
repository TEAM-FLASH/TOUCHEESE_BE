package com.team4.toucheese.studio.controller;

import com.team4.toucheese.studio.dto.StudioDto;
import com.team4.toucheese.studio.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/studio")
@RequiredArgsConstructor
public class StudioController {
    private final StudioService studioService;

    @GetMapping("")
    public Page<StudioDto> getAllStudios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "POPULARITY") StudioService.SortBy sortBy
    ){
        return studioService.getAllStudios(page, size, sortBy);
    }

    @GetMapping("/filter")
    public Page<StudioDto> getStudiosWithFilter(
            @RequestParam(required = false) LocalDateTime requestedDateTime,
            @RequestParam(required = false) LocalTime duration,
            @RequestParam(required = false) String vibeName,
            @RequestParam(required = false) String addressGu,
            Pageable pageable,
            @RequestParam(defaultValue = "POPULARITY") StudioService.SortBy sortBy
    ) {
        System.out.println("requestedDateTime = " + requestedDateTime);
        System.out.println("duration = " + duration);
        System.out.println("vibeName = " + vibeName);
        System.out.println("addressGu = " + addressGu);
        return studioService.getStudiosWithFilter(requestedDateTime, duration, vibeName, addressGu, pageable, sortBy);
    }
}

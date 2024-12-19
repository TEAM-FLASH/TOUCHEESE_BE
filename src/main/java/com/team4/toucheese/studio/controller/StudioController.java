package com.team4.toucheese.studio.controller;

import com.team4.toucheese.studio.dto.StudioDto;
import com.team4.toucheese.studio.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/studio")
@RequiredArgsConstructor
public class StudioController {
    private final StudioService studioService;

    @GetMapping("")
    public ResponseEntity<Page<StudioDto>> getAllStudios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "POPULARITY") StudioService.SortBy sortBy
    ){
        try {
            return ResponseEntity.ok(studioService.getAllStudios(page, size, sortBy));
        }catch ( ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch ( Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/filter")
//    public ResponseEntity<Page<StudioDto>> getStudiosWithFilter(
//            @RequestParam(required = false) LocalDateTime requestedDateTime,
//            @RequestParam(defaultValue = "01:00:00") // 디폴트 30분
//            LocalTime duration,
//            @RequestParam(required = false) String vibeName,
//            @RequestParam(required = false) String addressGu,
//            Pageable pageable,
//            @RequestParam(defaultValue = "POPULARITY") StudioService.SortBy sortBy,
//            @RequestParam(defaultValue = "-1")int minPrice,
//            @RequestParam(defaultValue = "-1")int maxPrice,
//            @RequestParam(required = false) String options
//    ) {
//        System.out.println("requestedDateTime = " + requestedDateTime);
//        System.out.println("duration = " + duration);
//        System.out.println("vibeName = " + vibeName);
//        System.out.println("addressGu = " + addressGu);
//        System.out.println("options = " + options);
//
//        try {
//            return ResponseEntity.ok(studioService.getStudiosWithFilter(requestedDateTime, duration, vibeName,
//                    addressGu, pageable, sortBy, minPrice, maxPrice,options));
//        }catch (ConfigDataResourceNotFoundException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }catch (IllegalArgumentException e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @GetMapping("/filter")
    public ResponseEntity<Page<StudioDto>> getStudiosWithFilter(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) List<LocalTime> times,
            @RequestParam(defaultValue = "30") // 디폴트 30분
            int duration,
            @RequestParam(required = false) String vibeName,
            @RequestParam(required = false) String addressGu,
            Pageable pageable,
            @RequestParam(defaultValue = "POPULARITY") StudioService.SortBy sortBy,
            @RequestParam(defaultValue = "-1")int minPrice,
            @RequestParam(defaultValue = "-1")int maxPrice,
            @RequestParam(required = false) String options
    ) {
        System.out.println("date = " + date);
        if (times == null) {
            System.out.println("null timeList ");
        }else {
            for (LocalTime time : times) {
                System.out.println("time = " + time);
            }
        }
        System.out.println("duration = " + duration);
        System.out.println("vibeName = " + vibeName);
        System.out.println("addressGu = " + addressGu);
        System.out.println("options = " + options);

        return ResponseEntity.ok(studioService.getStudiosWithFilter(date, times, duration, vibeName,
                    addressGu, pageable, sortBy, minPrice, maxPrice,options));
//        try {
//            return ResponseEntity.ok(studioService.getStudiosWithFilter(date, times, duration, vibeName,
//                    addressGu, pageable, sortBy, minPrice, maxPrice,options));
//        }catch (ConfigDataResourceNotFoundException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }catch (IllegalArgumentException e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }catch (Exception e){
//            System.out.println("e = " + e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
    }

    @GetMapping("/search")
    public ResponseEntity<Set<String>> getStudiosWithSearch(@RequestParam(defaultValue = "10") int topN){
        //Redis를 이용하여 실시간 검색어 노출
        try {
            return ResponseEntity.ok(studioService.getTopKeyword(topN));
        }catch (ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/result")
    public ResponseEntity<Page<StudioDto>> getStudiosWithSearchResult(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ){
        try {
            //검색어 저장
            studioService.saveKeyword(keyword);
            return ResponseEntity.ok(studioService.getStudiosWithSearch(keyword, pageable));
        }catch (ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

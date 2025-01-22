package com.team4.toucheese.studio.controller;

import com.team4.toucheese.studio.dto.AvailableTimeResultDto;
import com.team4.toucheese.studio.dto.ReservationRequest;
import com.team4.toucheese.studio.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/time")
    public ResponseEntity<AvailableTimeResultDto> getAvailableReservationTime(
            @RequestParam("studioId") Long studioId,
            @RequestParam("date") LocalDate date,
            @RequestParam("duration") Integer duration
    ){
        AvailableTimeResultDto result =  reservationService.getAvailableTime(date, studioId, duration);
        System.out.println(result);

        return ResponseEntity.ok(result);

        //Localdate NOW
        //  1. 가능한 날짜를 반환해준다

        //  2. 특정일이 선택이 되면 그 날에서 가능한 시간을 리턴해준다


    }

    @PostMapping("/action")
    public ResponseEntity<?> doReservation(
            @RequestBody ReservationRequest reservationRequest,
            Authentication authentication
            ){
        //예약하기
        System.out.println("reservationRequest = " + reservationRequest);
        System.out.println("authentication = " + authentication);
        String userEmail = "toucheese@gmail.com";
        if (authentication != null) {
            userEmail = authentication.getName();
        }
        try{
            reservationService.makeReservation(reservationRequest, userEmail);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

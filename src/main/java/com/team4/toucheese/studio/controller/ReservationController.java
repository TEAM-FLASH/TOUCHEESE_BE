package com.team4.toucheese.studio.controller;

import com.team4.toucheese.auth.dto.CustomUserDetails;
import com.team4.toucheese.studio.dto.AvailableTimeResultDto;
import com.team4.toucheese.studio.dto.ReservationCheckRequest;
import com.team4.toucheese.studio.dto.ReservationRequest;
import com.team4.toucheese.studio.entity.Reservation;
import com.team4.toucheese.studio.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
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
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            userEmail = userDetails.getEmail();
        }
        try{
            return ResponseEntity.ok(reservationService.makeReservation(reservationRequest, userEmail));
        }catch (ConfigDataResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/complete/{reservationId}")
    public ResponseEntity<?> completeReservation(@PathVariable("reservationId") Long reservationId){
        try{
            reservationService.completeReservation(reservationId);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/cancel/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable("reservationId") Long reservationId){
        try{
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> reservationCheck(@RequestBody ReservationCheckRequest reservationCheckRequest){
        try{
            return ResponseEntity.ok(reservationService.checkReservation(reservationCheckRequest.getReservationId()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

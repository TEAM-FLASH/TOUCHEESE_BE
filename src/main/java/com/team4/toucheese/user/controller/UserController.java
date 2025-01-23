package com.team4.toucheese.user.controller;

import com.team4.toucheese.user.dto.MyCanceledInfo;
import com.team4.toucheese.user.dto.MyCompletedInfo;
import com.team4.toucheese.user.dto.MyInfoDto;
import com.team4.toucheese.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/bookmark/{userId}/{studioId}")
    public ResponseEntity<String> addBookMark(@PathVariable("userId") Long userId, @PathVariable("studioId") Long studioId){
        try{
            userService.addBookMark(userId, studioId);
            return ResponseEntity.ok("success");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/bookmark/{userId}/{studioId}")
    public ResponseEntity<String> deleteBookMark(@PathVariable("userId") Long userId, @PathVariable("studioId") Long studioId){
        try{
            userService.deleteBookMark(userId, studioId);
            return ResponseEntity.ok("success");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> myPage(Authentication authentication){
        try{
            System.out.println("authentication = " + authentication.getPrincipal());
            List<MyInfoDto> myInfoDtos = userService.getMyInfo(authentication);
            return ResponseEntity.ok(myInfoDtos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mypage/reservation")
    public ResponseEntity<?> myReservation(Authentication authentication){
        try{
            List<MyInfoDto> myInfoDtos = userService.getMyReservation(authentication);
            return ResponseEntity.ok(myInfoDtos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mypage/reservation/completed")
    public ResponseEntity<?> myReservationCompleted(Authentication authentication){
        try{
            List<MyCompletedInfo> myCompletedInfos = userService.getMyComplete(authentication);
            return ResponseEntity.ok(myCompletedInfos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mypage/reservation/canceled")
    public ResponseEntity<?> myReservationCanceled(Authentication authentication){
        try{
            List<MyCanceledInfo> myCanceledInfos = userService.getMyCancel(authentication);
            return ResponseEntity.ok(myCanceledInfos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

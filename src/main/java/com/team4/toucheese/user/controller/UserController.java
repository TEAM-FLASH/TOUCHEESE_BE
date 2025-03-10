package com.team4.toucheese.user.controller;

import com.team4.toucheese.user.dto.*;
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

    @GetMapping("/mypage/reservation/reserved")
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
            System.out.println("e = " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mypage/reservation/completed")
    public ResponseEntity<?> myReservationCompleted(Authentication authentication){
        try{
            List<MyInfoDto> myCompletedInfos = userService.getMyComplete(authentication);
            return ResponseEntity.ok(myCompletedInfos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mypage/reservation/canceled")
    public ResponseEntity<?> myReservationCanceled(Authentication authentication){
        try{
            List<MyInfoDto> myCanceledInfos = userService.getMyCancel(authentication);
            return ResponseEntity.ok(myCanceledInfos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/mypage/changepw")
    public ResponseEntity<?> changePassword(Authentication authentication, ChangePasswordRequest changePasswordRequest){
        try {
            return ResponseEntity.ok(userService.changePassword(authentication, changePasswordRequest.getNewPassword()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/mypage/changeph")
    public ResponseEntity<?> changePhone(Authentication authentication, ChangePhoneRequest changePhoneRequest){
        try{
            return ResponseEntity.ok(userService.changePhone(authentication, changePhoneRequest.getNewPhone()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

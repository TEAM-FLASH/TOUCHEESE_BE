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

    @PostMapping("/bookmark/{studioId}")
    public ResponseEntity<BookmarkResultDto> addBookMark(Authentication authentication, @PathVariable("studioId") Long studioId){
        try{
            return ResponseEntity.ok(userService.addBookMark(authentication, studioId));
        }catch (Exception e){
            BookmarkResultDto bookmarkResultDto = new BookmarkResultDto();
            bookmarkResultDto.setType("add");
            bookmarkResultDto.setMessage(e.getMessage());
            bookmarkResultDto.setSuccess(false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bookmarkResultDto);
        }
    }
    @DeleteMapping("/bookmark/{studioId}")
    public ResponseEntity<BookmarkResultDto> deleteBookMark(Authentication authentication, @PathVariable("studioId") Long studioId){
        try{
            return ResponseEntity.ok(userService.deleteBookMark(authentication, studioId));
        }catch (Exception e){
            BookmarkResultDto bookmarkResultDto = new BookmarkResultDto();
            bookmarkResultDto.setType("delete");
            bookmarkResultDto.setMessage(e.getMessage());
            bookmarkResultDto.setSuccess(false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bookmarkResultDto);
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
    public ResponseEntity<?> changePassword(Authentication authentication,
                                            @RequestBody
                                            ChangePasswordRequest changePasswordRequest){
        try {
//            System.out.println("changePasswordRequest = " + changePasswordRequest.getNewPassword());
            return ResponseEntity.ok(userService.changePassword(authentication, changePasswordRequest.getNewPassword()));
        }catch (Exception e){
            System.out.println("e = " + e.getMessage());
            ChangePasswordResultDTO changePasswordResultDTO = new ChangePasswordResultDTO();
            changePasswordResultDTO.setSuccess(false);
            changePasswordResultDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(changePasswordResultDTO);
        }
    }

    @PostMapping("/mypage/changeph")
    public ResponseEntity<?> changePhone(Authentication authentication,
                                         @RequestBody
                                         ChangePhoneRequest changePhoneRequest){
        try{
            return ResponseEntity.ok(userService.changePhone(authentication, changePhoneRequest.getNewPhone()));
        }catch (Exception e){
            ChangePhoneResultDTO changePhoneResultDTO = new ChangePhoneResultDTO();
            changePhoneResultDTO.setSuccess(false);
            changePhoneResultDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(changePhoneResultDTO);
        }
    }
}

package com.team4.toucheese.user.controller;

import com.team4.toucheese.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

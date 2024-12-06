package com.team4.toucheese.user.controller;

import com.team4.toucheese.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/bookmark/{userId}/{studioId}")
    public void addBookMark(@PathVariable("userId") Long userId, @PathVariable("studioId") Long studioId){
        userService.addBookMark(userId, studioId);
    }
    @DeleteMapping("/bookmark/{userId}/{studioId}")
    public void deleteBookMark(@PathVariable("userId") Long userId, @PathVariable("studioId") Long studioId){
        userService.deleteBookMark(userId, studioId);
    }
}

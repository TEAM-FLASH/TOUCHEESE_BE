//package com.team4.toucheese.user.controller;
//
//import com.team4.toucheese.auth.service.JpaUserDetailsManager;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class LoginController {
//
//    private final PasswordEncoder passwordEncoder;
////    private final UserDetailsManager manager;
//    private final JpaUserDetailsManager manager;
//
//    @GetMapping("/login")
//    public String login() {
//        return "login-form";
//    }
//
//    @GetMapping("/my-profile")
//    public String myProfile(Authentication authentication) {
//        System.out.println(authentication.getName());
//        System.out.println(authentication.getPrincipal());
//        return "my-profile";
//    }
//
//    @GetMapping("/register")
//    public String register() {
//        return "register-form";
//    }
//
//    @PostMapping("/register")
//    public String registerPost(
//            @RequestParam("username") String username,
//            @RequestParam("password") String password,
////            @RequestParam("email") String email,
//            @RequestParam("password-check") String passwordCheck
//    ) {
//        if (password.equals(passwordCheck)){
//            manager.createUser(username, password, passwordCheck);
//        }
//        return "redirect:/auth/login";
//    }
//
//}

package com.team4.toucheese.auth.dto;

import lombok.Data;

@Data
public class CreateUserDto {
    private String userName;
    private String email;
    private String password;
    private String phone;
    private String registration;
}

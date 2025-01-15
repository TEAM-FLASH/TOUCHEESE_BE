package com.team4.toucheese.auth.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String status;
    private String r_username;
    private String r_email;
    private String r_registration;
    private String r_password;
}

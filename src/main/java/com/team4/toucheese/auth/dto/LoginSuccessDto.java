package com.team4.toucheese.auth.dto;

import lombok.Data;

@Data
public class LoginSuccessDto {
    private String accessToken;
    private Long user_id;
    private String username;
    private String email;
    private String phone;
    private String registration;
}

package com.team4.toucheese.auth.dto;

import lombok.Data;

@Data
public class LoginDto {
    private final String email;
    private final String password;
}

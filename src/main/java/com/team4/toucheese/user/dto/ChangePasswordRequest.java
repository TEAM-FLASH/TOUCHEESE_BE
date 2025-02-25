package com.team4.toucheese.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    private String newPassword;
}

package com.team4.toucheese.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePhoneRequest {
    private String newPhone;
}

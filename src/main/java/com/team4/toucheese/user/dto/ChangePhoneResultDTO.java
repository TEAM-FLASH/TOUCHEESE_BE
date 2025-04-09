package com.team4.toucheese.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePhoneResultDTO {
    private boolean success;
    private String message;
}

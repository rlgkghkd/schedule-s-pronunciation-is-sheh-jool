package com.example.schedule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    @Max(20)
    private String userName;
    @Email
    private String userMail;
    @NotEmpty
    private String password;
}

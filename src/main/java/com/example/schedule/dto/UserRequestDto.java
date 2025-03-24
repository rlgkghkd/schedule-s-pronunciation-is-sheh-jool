package com.example.schedule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserRequestDto {
    @Max(20)
    @NotEmpty
    private String userName;
    @Email
    @NotEmpty
    private String userMail;
    @NotEmpty
    private String password;
}

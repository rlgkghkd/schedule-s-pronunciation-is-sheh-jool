package com.example.schedule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserCreateRequestDto {
    @Max(value = 20, message = "20자 이하로 입력해주세요.")
    @NotEmpty(message = "userName은 공백일 수 없습니다.")
    private String userName;
    @Email(message = "올바른 이메일이 아닙니다.")
    @NotEmpty(message = "userMail은 공백일 수 없습니다.")
    private String userMail;
    @NotEmpty(message = "password는 공백일 수 없습니다.")
    @NotNull(message = "password는 null일 수 없습니다.")
    private String password;
}

package com.example.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleCreateRequestDto {

    @Size(max=200, message = "200자 이하로 작성해주세요.")
    @NotEmpty(message = "plan은 공백일 수 없습니다.")
    private String plan;
    @NotNull(message = "id는 필수 입력값 입니다.")
    private Long userId;
    @NotEmpty(message = "password는 공백일 수 없습니다.")
    @NotNull(message = "password는 null일 수 없습니다.")
    private String password;
}

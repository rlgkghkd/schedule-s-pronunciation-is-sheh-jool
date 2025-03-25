package com.example.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleUpdateRequestDto {

    @Size(max=200)
    private String plan;
    private Long userId;
    @NotEmpty
    @NotNull
    private String password;
}

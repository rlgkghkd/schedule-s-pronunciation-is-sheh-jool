package com.example.schedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    @Size(max=200)
    @NotEmpty
    private String plan;
    @NotNull
    private Long userId;
    @NotEmpty
    private String password;
}

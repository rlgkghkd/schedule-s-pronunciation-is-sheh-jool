package com.example.schedule.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private String plan;
    private Long userId;
    private String password;
}

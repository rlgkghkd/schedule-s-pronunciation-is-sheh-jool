package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;

import java.util.Date;
import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> getSchedule(Long id, String todo, String name, Date writeDate, Date rewriteDate);
    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto);
    void deleteSchedule(Long id, ScheduleRequestDto dto);
}

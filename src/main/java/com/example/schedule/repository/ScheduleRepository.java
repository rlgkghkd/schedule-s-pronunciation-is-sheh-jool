package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;

import java.util.Date;
import java.util.List;


public interface ScheduleRepository {
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto);
    public List<ScheduleResponseDto> getSchedule(Long id, String todo, String name, Date writeDate, Date rewriteDate);
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto);

}

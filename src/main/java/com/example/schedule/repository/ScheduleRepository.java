package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;

import java.util.Date;
import java.util.List;


public interface ScheduleRepository {
    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);
    List<Schedule> getSchedule(Long id, String todo, String name, Date writeDate, Date rewriteDate);
    int updateSchedule(Long id, ScheduleRequestDto dto);
    int deleteSchedule(Long id, ScheduleRequestDto dto);
}

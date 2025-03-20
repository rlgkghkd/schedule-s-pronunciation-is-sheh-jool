package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScheduleServiceImp implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImp(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto) {
        return scheduleRepository.createSchedule(dto);
    }

    @Override
    public List<ScheduleResponseDto> getSchedule(Long id, String todo, String name, Date writeDate, Date rewriteDate) {
        return scheduleRepository.getSchedule(id, todo, name, writeDate, rewriteDate);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {
        return scheduleRepository.updateSchedule(id, dto);
    }
}
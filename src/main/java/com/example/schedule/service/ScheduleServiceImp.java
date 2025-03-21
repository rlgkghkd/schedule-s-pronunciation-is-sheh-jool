package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        return scheduleRepository.getSchedule(id, todo, name, writeDate, rewriteDate).stream().map(ScheduleResponseDto::new).toList();
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {
        int index= scheduleRepository.updateSchedule(id, dto);
        Schedule schedule= scheduleRepository.getSchedule(id, null, null, null, null).get(0);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public void deleteSchedule(Long id, ScheduleRequestDto dto) {
        scheduleRepository.deleteSchedule(id, dto);
        if (id== 0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
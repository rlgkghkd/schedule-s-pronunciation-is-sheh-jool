package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.User;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
    public List<UserResponseDto> getUser(Long userId, String userName) {
        return scheduleRepository.getUser(userId, userName).stream().map(UserResponseDto::new).toList();
    }

    @Override
    public List<ScheduleResponseDto> getSchedule(Long id, String plan, Long userId, Date createdDate, Date editedDate) {
        List<ScheduleResponseDto> returnList = new ArrayList<>();
        List<Schedule> scheduleList =scheduleRepository.getSchedule(id, plan, userId, createdDate, editedDate);
        for (Schedule s: scheduleList){
            returnList.add(new ScheduleResponseDto(s, scheduleRepository.getUser(s.getUserId(), null).get(0).getUserName()));
        }
        return returnList;
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {
        scheduleRepository.updateSchedule(id, dto);
        Schedule schedule= scheduleRepository.getSchedule(id, null, null, null, null).get(0);
        User user= scheduleRepository.getUser(dto.getUserId(), null).get(0);
        return new ScheduleResponseDto(schedule, user.getUserName());
    }

    @Override
    public void deleteSchedule(Long id, ScheduleRequestDto dto) {
        scheduleRepository.deleteSchedule(id, dto);
        if (id== 0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
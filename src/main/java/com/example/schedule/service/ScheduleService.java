package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserRequestDto;
import com.example.schedule.dto.UserResponseDto;

import java.util.Date;
import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);
    UserResponseDto createUser(UserRequestDto dto);
    List<UserResponseDto> getUser(Long userId, String userName);
    List<ScheduleResponseDto> getSchedule(Long id, String plan, Long userId, Date createdDate, Date editedDate);
    UserResponseDto updateUser(Long id, UserRequestDto dto);
    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto);
    void deleteUser(Long id, UserRequestDto dto);
    void deleteSchedule(Long id, ScheduleRequestDto dto);
}

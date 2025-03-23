package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserRequestDto;
import com.example.schedule.dto.UserResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.User;

import java.util.Date;
import java.util.List;


public interface ScheduleRepository {
    UserResponseDto createUser(UserRequestDto dto);
    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);
    List<User> getUser(Long userId, String userName);
    List<Schedule> getSchedule(Long id, String plan, Long userId, Date createdDate, Date editedDate);
    int updateUser(Long id, UserRequestDto dto);
    int updateSchedule(Long id, ScheduleRequestDto dto);
    int deleteUser(Long id, UserRequestDto dto);
    int deleteSchedule(Long id, ScheduleRequestDto dto);
}

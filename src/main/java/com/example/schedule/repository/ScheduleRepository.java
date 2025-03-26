package com.example.schedule.repository;

import com.example.schedule.dto.*;

import java.util.Date;
import java.util.List;


public interface ScheduleRepository {
    UserResponseDto createUser(UserCreateRequestDto dto);
    ScheduleResponseDto createSchedule(ScheduleCreateRequestDto dto);
    List<UserResponseDto> getUser(Long userId, String userName);
    List<ScheduleResponseDto> getSchedule(Long id, String plan, Long userId, Date createdDate, Date editedDate);
    int updateUser(Long id, UserUpdateRequestDto dto);
    int updateSchedule(Long id, ScheduleUpdateRequestDto dto);
    int deleteUser(Long id, UserCreateRequestDto dto);
    int deleteSchedule(Long id, ScheduleCreateRequestDto dto);
}

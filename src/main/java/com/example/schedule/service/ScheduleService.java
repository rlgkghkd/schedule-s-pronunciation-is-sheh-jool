package com.example.schedule.service;

import com.example.schedule.dto.*;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleCreateRequestDto dto);
    UserResponseDto createUser(UserCreateRequestDto dto);
    List<UserResponseDto> getUser(Long userId, String userName);
    List<ScheduleResponseDto> getSchedule(Long id, String plan, Long userId, Date createdDate, Date editedDate);
    UserResponseDto updateUser(Long id, UserUpdateRequestDto dto);
    ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto dto);
    void deleteUser(Long id, UserCreateRequestDto dto);
    void deleteSchedule(Long id, ScheduleCreateRequestDto dto);
    Page <UserResponseDto> pagingUser(List<UserResponseDto> list, int pageIndex, int pageSize);
    Page<ScheduleResponseDto> pagingSchedule(List<ScheduleResponseDto> list, int pageIndex, int pageSize);
}

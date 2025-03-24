package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserRequestDto;
import com.example.schedule.dto.UserResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.User;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    public UserResponseDto createUser(UserRequestDto dto) {
        return scheduleRepository.createUser(dto);
    }

    @Override
    public List<UserResponseDto> getUser(Long userId, String userName) {
        List<UserResponseDto> returnList=scheduleRepository.getUser(userId, userName).stream().map(UserResponseDto::new).toList();
        if (returnList.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        return returnList;
    }

    @Override
    public List<ScheduleResponseDto> getSchedule(Long id, String plan, Long userId, Date createdDate, Date editedDate) {
        List<ScheduleResponseDto> returnList = new ArrayList<>();
        List<Schedule> scheduleList =scheduleRepository.getSchedule(id, plan, userId, createdDate, editedDate);
        for (Schedule s: scheduleList){
            returnList.add(new ScheduleResponseDto(s, scheduleRepository.getUser(s.getUserId(), null).get(0).getUserName()));
        }
        if (returnList.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        return returnList;
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {

        int amount= scheduleRepository.updateUser(id, dto);
        if (amount==0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        return new UserResponseDto(scheduleRepository.getUser(id, null).get(0));
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {
        int amount=scheduleRepository.updateSchedule(id, dto);
        if (amount==0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        Schedule schedule= scheduleRepository.getSchedule(id, null, null, null, null).get(0);
        User user= scheduleRepository.getUser(dto.getUserId(), null).get(0);
        return new ScheduleResponseDto(schedule, user.getUserName());
    }

    @Override
    public void deleteUser(Long id, UserRequestDto dto) {
       int amount= scheduleRepository.deleteUser(id, dto);
        if (amount==0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
    }

    @Override
    public void deleteSchedule(Long id, ScheduleRequestDto dto) {
        int amount= scheduleRepository.deleteSchedule(id, dto);
        if (amount==0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
    }

    @Override
    public Page<UserResponseDto> pagingUser(List<UserResponseDto> list, int pageIndex, int pageSize) {
        int start= Math.min((pageIndex -1) * pageSize, list.size()-1);
        int end= Math.min(start + pageSize, list.size());
        List <UserResponseDto> paged= list.subList(start, end);
        return new PageImpl<>(paged,PageRequest.of((pageIndex -1), pageSize), list.size());
    }

    @Override
    public Page<ScheduleResponseDto> pagingSchedule(List<ScheduleResponseDto> list, int pageIndex, int pageSize) {
        int start= Math.min((pageIndex -1) * pageSize, list.size()-1);
        int end= Math.min(start + pageSize, list.size());
        List <ScheduleResponseDto> paged= list.subList(start, end);
        return new PageImpl<>(paged,PageRequest.of((pageIndex -1), pageSize), list.size());
    }
}
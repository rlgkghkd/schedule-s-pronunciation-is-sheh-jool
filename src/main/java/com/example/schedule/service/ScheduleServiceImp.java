package com.example.schedule.service;

import com.example.schedule.dto.*;
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

    // ScheduleRepository 싱글톤
    private final ScheduleRepository scheduleRepository;
    public ScheduleServiceImp(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 스케줄 생성
    @Override
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto dto) {
        return scheduleRepository.createSchedule(dto);
    }

    // 유저 생성
    @Override
    public UserResponseDto createUser(UserCreateRequestDto dto) {
        return scheduleRepository.createUser(dto);
    }

    // 유저 검색
    // 검색 결과는 리스트로 받음
    @Override
    public List<UserResponseDto> getUser(Long userId, String userName) {
        return scheduleRepository.getUser(userId, userName);
    }

    // 스케줄 검색
    // 검색 결과는 리스트로 받음
    // ScheduleResponseDto는 user테이블의 USER_NAME을 참조하므로 스케줄 테이블과 유저 테이블 검색결과를 모두 가져옴.
    // 리스트가 비어있는 경우 NOT_FOUND 예외 반환
    @Override
    public List<ScheduleResponseDto> getSchedule(Long id, String plan, Long userId, Date createdDate, Date editedDate) {
        return scheduleRepository.getSchedule(id, plan, userId, createdDate, editedDate);
    }

    // 유저 업데이트
    // amount는 업데이트된 행의 수.
    // amount가 0이면 NOT_FOUND 예외 반환
    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto dto) {
        int amount= scheduleRepository.updateUser(id, dto);
        if (amount==0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        return scheduleRepository.getUser(id, null).get(0);
    }

    // 스케줄 업데이트
    // amount는 업데이트된 행의 수.
    // amount가 0이면 NOT_FOUND 예외 반환
    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto dto) {
        int amount=scheduleRepository.updateSchedule(id, dto);
        if (amount==0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        return scheduleRepository.getSchedule(id, null, null, null, null).get(0);
    }

    // 유저 삭제
    // amount는 삭제된 행의 수.
    // amount가 0이면 NOT_FOUND 예외 반환
    @Override
    public void deleteUser(Long id, UserCreateRequestDto dto) {
       int amount= scheduleRepository.deleteUser(id, dto);
        if (amount==0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
    }

    // 스케줄 삭제
    // amount는 삭제된 행의 수.
    // amount가 0이면 NOT_FOUND 예외 반환
    @Override
    public void deleteSchedule(Long id, ScheduleCreateRequestDto dto) {
        int amount= scheduleRepository.deleteSchedule(id, dto);
        if (amount==0){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
    }

    // 유저 검색결과 페이질
    @Override
    public Page<UserResponseDto> pagingUser(List<UserResponseDto> list, int pageIndex, int pageSize) {
        int start= Math.min((pageIndex -1) * pageSize, list.size()-1);
        int end= Math.min(start + pageSize, list.size());
        List <UserResponseDto> paged= list.subList(start, end);
        return new PageImpl<>(paged,PageRequest.of((pageIndex -1), pageSize), list.size());
    }

    // 스케줄 검색결과 페이징
    @Override
    public Page<ScheduleResponseDto> pagingSchedule(List<ScheduleResponseDto> list, int pageIndex, int pageSize) {
        int start= Math.min((pageIndex -1) * pageSize, list.size()-1);
        int end= Math.min(start + pageSize, list.size());
        List <ScheduleResponseDto> paged= list.subList(start, end);
        return new PageImpl<>(paged,PageRequest.of((pageIndex -1), pageSize), list.size());
    }
}
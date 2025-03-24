package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserRequestDto;
import com.example.schedule.dto.UserResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.User;
import com.example.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.relational.core.sql.Assignment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

// 뷰 안쓰니까 Rest
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto){
        return new ResponseEntity<>(scheduleService.createUser(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedule(
            @Valid @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "plan", required = false) String plan,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "createdDate", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date createdDate,
            @RequestParam(value = "editedDate", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date editedDate,
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex
            ){
        List<ScheduleResponseDto> returnedList = scheduleService.getSchedule(id, plan, userId, createdDate, editedDate);
        Page<ScheduleResponseDto> page=  scheduleService.pagingSchedule(returnedList, pageIndex, 2);
        return new ResponseEntity<>(page.stream().toList(), HttpStatus.FOUND);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> getUser(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "pageIndex") int pageIndex){
        List<UserResponseDto> returnedList = scheduleService.getUser(userId, userName);
        Page<UserResponseDto> page=  scheduleService.pagingUser(returnedList, pageIndex, 2);
        return new ResponseEntity<>(page.stream().toList(), HttpStatus.FOUND);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> patchUser(@PathVariable Long id, @RequestBody UserRequestDto dto){
        return new ResponseEntity<>(scheduleService.updateUser(id, dto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> patchSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public  void  deleteUser(@PathVariable Long id, @RequestBody UserRequestDto dto){
        scheduleService.deleteUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public  void  deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto dto){
        scheduleService.deleteSchedule(id, dto);
    }
}

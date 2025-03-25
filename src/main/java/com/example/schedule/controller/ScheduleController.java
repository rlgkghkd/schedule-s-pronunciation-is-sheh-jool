package com.example.schedule.controller;

import com.example.schedule.dto.*;
import com.example.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

// 뷰 안쓰니까 Rest
@RestController
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Validated @RequestBody ScheduleCreateRequestDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult.getAllErrors().get(0).getDefaultMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> createUser(@Validated @RequestBody UserCreateRequestDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult.getAllErrors().get(0).getDefaultMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<UserResponseDto> patchUser(@PathVariable Long id, @Validated @RequestBody UserUpdateRequestDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult.getAllErrors().get(0).getDefaultMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(scheduleService.updateUser(id, dto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> patchSchedule(@PathVariable Long id, @Validated @RequestBody ScheduleUpdateRequestDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult.getAllErrors().get(0).getDefaultMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public  void  deleteUser(@PathVariable Long id, @RequestBody UserCreateRequestDto dto){
        if (id == 0){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}
        scheduleService.deleteUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public  void  deleteSchedule(@PathVariable Long id, @RequestBody ScheduleCreateRequestDto dto){
        if (id == 0){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}
        scheduleService.deleteSchedule(id, dto);
    }
}

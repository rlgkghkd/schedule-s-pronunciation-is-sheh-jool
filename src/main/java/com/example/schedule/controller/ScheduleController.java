package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserResponseDto;
import com.example.schedule.entity.User;
import com.example.schedule.service.ScheduleService;
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
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedule(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "plan", required = false) String plan,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "CREATED_DATE", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd::HH:mm:ss")Date createdDate,
            @RequestParam(value = "EDITED_DATE", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd::HH:mm:ss")Date editedDate
            ){
            return new ResponseEntity<>(scheduleService.getSchedule(id, plan, userId, createdDate, editedDate), HttpStatus.FOUND);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> getSchedule(@RequestParam Long userId, @RequestParam String userName){
        return new ResponseEntity<>(scheduleService.getUser(userId, userName), HttpStatus.FOUND);
    }



    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> patchSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public  void  deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto dto){
        scheduleService.deleteSchedule(id, dto);
    }
}

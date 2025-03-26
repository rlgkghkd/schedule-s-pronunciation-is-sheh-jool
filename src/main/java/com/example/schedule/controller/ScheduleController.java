package com.example.schedule.controller;

import com.example.schedule.dto.*;
import com.example.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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

    //ScheduleService 싱글톤 생성
    private final ScheduleService scheduleService;
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    // 스케줄 생성
    // ScheduleCreateDto 양식을 통해 생성, 반환은 ScheduleResponseDto
    // userId는 user테이블에서 이미 생성된 유저의 id만 사용 가능, password는 user테이블에 user가 가진 password를 등록해야 함.
    @PostMapping
    public Object createSchedule(@Validated @RequestBody ScheduleCreateRequestDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        }
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }

    // 유저 생성
    // UserCreateDto 양식을 통해 생성, 반환은 UserResponseDto
    @PostMapping("/user")
    public Object createUser(@Validated @RequestBody UserCreateRequestDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        }
        return new ResponseEntity<>(scheduleService.createUser(dto), HttpStatus.CREATED);
    }

    // 스케줄 검색
    // 검색할 항목과 페이지 인덱스를 파라미터로 전달. 검색이 필요하지 않은 항목은 생략 가능
    // 생략된 파라미터는 null값으로 전달됨. 전달된 null값은 repository에서 처리
    // 반환된 ResponseDto List는 페이지화해서 리스트로 재변환.
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedule(
            @Valid @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "plan", required = false) String plan,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "createdDate", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date createdDate,
            @RequestParam(value = "editedDate", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date editedDate,
            // 페이지 시작은 0부터. service에서 받아서 -1 함.
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex
            ){
        List<ScheduleResponseDto> returnedList = scheduleService.getSchedule(id, plan, userId, createdDate, editedDate);
        Page<ScheduleResponseDto> page=  scheduleService.pagingSchedule(returnedList, pageIndex, 2);
        // 페이지 객체를 그대로 반환하면 페이지 세팅 정보가 노출되어 리스트 형태로 반환
        // spring에서 페이지를 반환하면서 정보를 노출하지 않게 하는 방법을 지원하는것 같은데, 구현하지 못함.
        return new ResponseEntity<>(page.stream().toList(), HttpStatus.FOUND);
    }


    // 유저 검색
    // 작동은 스케줄 검색과 동일하게 작동.
    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> getUser(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex){
        List<UserResponseDto> returnedList = scheduleService.getUser(userId, userName);
        Page<UserResponseDto> page=  scheduleService.pagingUser(returnedList, pageIndex, 2);
        return new ResponseEntity<>(page.stream().toList(), HttpStatus.FOUND);
    }

    // 유저 수정
    // UserUpdateRequestDto 사용
    // password를 제외한 값들은 필수값이 아님.
    @PatchMapping("/user/{id}")
    public Object patchUser(@PathVariable Long id, @Validated @RequestBody UserUpdateRequestDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        }
        return new ResponseEntity<>(scheduleService.updateUser(id, dto), HttpStatus.OK);
    }

    // 스케줄 수정
    // ScheduleUpdateRequestDto 사용
    // password를 제외한 값들은 필수값이 아님. password는 수정 전 스케줄 소유 유저의 password
    // 수정할 userId는 user 테이블에 이미 존재하는 값만 사용 가능
    @PatchMapping("/{id}")
    public Object patchSchedule(@PathVariable Long id, @Validated @RequestBody ScheduleUpdateRequestDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        }
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto), HttpStatus.OK);
    }

    // 유저 삭제
    @DeleteMapping("/user/{id}")
    public  void  deleteUser(@PathVariable Long id, @RequestBody UserCreateRequestDto dto){
        if (id == 0){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}
        scheduleService.deleteUser(id, dto);
    }

    // 스케줄 삭제
    @DeleteMapping("/{id}")
    public  void  deleteSchedule(@PathVariable Long id, @RequestBody ScheduleCreateRequestDto dto){
        if (id == 0){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}
        scheduleService.deleteSchedule(id, dto);
    }
}

package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;


@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private String plan;
    private Date createdDate;
    private Date editedDate;

    public ScheduleResponseDto(Schedule schedule){
        this.id= schedule.getId();
        this.userId= schedule.getUserId();
        this.userName= getUserName();
        this.plan = schedule.getPlan();
        this.createdDate = schedule.getCreatedDate();
        this.editedDate = schedule.getEditedDate();
    }

    public ScheduleResponseDto(Schedule schedule, String name){
        this.id= schedule.getId();
        this.userId= schedule.getUserId();
        this.userName= name;
        this.plan = schedule.getPlan();
        this.createdDate = schedule.getCreatedDate();
        this.editedDate = schedule.getEditedDate();
    }
}

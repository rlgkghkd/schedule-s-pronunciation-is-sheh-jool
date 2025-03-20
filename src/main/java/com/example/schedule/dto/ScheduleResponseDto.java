package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;


@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String todo;
    private String name;
    private Date writeDate;
    private Date rewriteDate;

    public ScheduleResponseDto(Schedule schedule){
        this.id= schedule.getId();
        this.todo= schedule.getTodo();
        this.name= schedule.getName();
        this.writeDate= schedule.getWriteDate();
        this.rewriteDate= schedule.getRewriteDate();
    }
}

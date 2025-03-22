package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long id;
    private Long userId;
    private String plan;
    private Date createdDate;
    private Date editedDate;

    public Schedule(Schedule schedule){
        this.id = schedule.getId();
        this.userId= schedule.getUserId();
        this.plan= schedule.getPlan();
        this.createdDate = schedule.getCreatedDate();
        this.editedDate = schedule.getEditedDate();
    }
}


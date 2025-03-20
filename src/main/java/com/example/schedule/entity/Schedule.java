package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long id;
    private String todo;
    private String name;
    private Long password;
    private Date writeDate;
    private Date rewriteDate;

    public Schedule(long id, String todo, String name, java.sql.Date writeDate, java.sql.Date rewriteDate) {
        this.id =id;
        this.todo= todo;
        this.name= name;
        this.writeDate= writeDate;
        this.rewriteDate= rewriteDate;
    }

    public Schedule(Schedule schedule){
        this.id = getId();
        this.todo= getTodo();
        this.name= getName();
        this.writeDate= getWriteDate();
        this.rewriteDate= getRewriteDate();
    }
}


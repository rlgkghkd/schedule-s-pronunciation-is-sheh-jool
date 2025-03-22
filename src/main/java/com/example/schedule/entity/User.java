package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class User {
    private Long userId;
    private String userName;
    private String userMail;
    private Date registedDate;
    private Date editedDate;
    private String password;

    public User (User user){
        this.userId= user.getUserId();
        this.userName= user.getUserName();
        this.userMail= user.getUserMail();
        this.registedDate= user.getRegistedDate();
        this.editedDate= user.getEditedDate();
        this.password= user.getPassword();
    }

}

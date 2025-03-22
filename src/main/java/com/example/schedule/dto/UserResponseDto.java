package com.example.schedule.dto;

import com.example.schedule.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String userName;
    private String userMail;
    private Date registedDate;
    private Date editedDate;

    public UserResponseDto (User user) {
        this.userId= user.getUserId();
        this.userName= user.getUserName();
        this.userMail= user.getUserMail();
        this.registedDate= user.getRegistedDate();
        this.editedDate= user.getEditedDate();
    }
}

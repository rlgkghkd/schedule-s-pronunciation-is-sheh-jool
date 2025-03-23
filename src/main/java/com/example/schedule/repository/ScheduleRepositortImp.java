package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.UserRequestDto;
import com.example.schedule.dto.UserResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Repository
public class ScheduleRepositortImp implements ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepositortImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        SimpleJdbcInsert simpleJdbcInsert= new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("user").usingGeneratedKeyColumns("USER_ID");

        Map<String, Object> param= new HashMap<>();
        Date now= new Date();

        param.put("userName", dto.getUserName());
        param.put("userMail", dto.getUserMail());
        param.put("registedDate", now);
        param.put("editedDate", now);
        param.put("password", dto.getPassword());

        Number key= simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(param));
        return new UserResponseDto(key.longValue(), dto.getUserName(), dto.getUserMail(), now, now);
    }

    @Override
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto) {

        SimpleJdbcInsert simpleJdbcInsert= new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("ID");

        Map<String, Object> param= new HashMap<>();
        Date now= new Date();
        User user= jdbcTemplate.query("select * from user where USER_ID = ?", userRowMapper(), dto.getUserId()).get(0);
        if(!user.getPassword().equals(dto.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}

        param.put("userId", dto.getUserId());
        param.put("plan", dto.getPlan());
        param.put("createdDate", now);
        param.put("editedDate", now);
        Number key= 0;

        try {key= simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(param));}
        catch (Exception e) {System.out.println(e);}
        return new ScheduleResponseDto(key.longValue(), user.getUserId(), user.getUserName(), dto.getPlan(), now, now);
    }

    @Override
    public List<User> getUser(Long userId, String userName) {
        String sqlQuery="select * from user where 1=1";
        sqlQuery= userId==null?sqlQuery+" and 1 is Not ?": sqlQuery+" and USER_ID = ?";
        sqlQuery= userName==null?sqlQuery+" and 1 is Not ?": sqlQuery+" and USER_NAME = ?";
        System.out.println(sqlQuery);


        List<User> userList= jdbcTemplate.query(sqlQuery, userRowMapper(), userId, userName);
        if (userList.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        return userList;
    }


    @Override
    public List<Schedule> getSchedule(Long id, String plan, Long userId, Date createdDate, Date editedDate) {
        String sqlQuery="select * from schedule where 1=1";
        sqlQuery= id==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and ID = ?";
        sqlQuery= plan ==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and PLAN = ?";
        sqlQuery= userId==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and USER_ID = ?";
        sqlQuery= createdDate==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and DATE_FORMAT(CREATED_DATE, '%Y-%m-%d') = ?";
        sqlQuery= editedDate==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and DATE_FORMAT(EDITED_DATE, '%Y-%m-%d') = ?";
        System.out.println(createdDate +" "+ editedDate);
        System.out.println(sqlQuery);

        String created= createdDate==null?null: new SimpleDateFormat("yyyy-MM-dd").format(createdDate);
        String edited= editedDate==null?null:new SimpleDateFormat("yyyy-MM-dd").format(editedDate);

        List<Schedule> scheduleList= jdbcTemplate.query(sqlQuery, scheduleRowMapper(), id, plan, userId, created, edited);
        if (scheduleList.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        return scheduleList;
    }

    @Override
    public int updateUser(Long id, UserRequestDto dto) {
        User user= jdbcTemplate.query("select * from user where USER_ID = ?", userRowMapper(), id).get(0);
        if (!Objects.equals(dto.getPassword(), user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}

        Date now= new Date();
        int index=0;
        if (dto.getUserName() != null){index=jdbcTemplate.update("update user set USER_NAME= ? , EDITED_DATE = ? where USER_ID= ?", dto.getUserName(),now, id);}
        if (dto.getUserMail() != null){index=jdbcTemplate.update("update user set USER_MAIL= ? , EDITED_DATE = ? where USER_ID= ?", dto.getUserMail(),now, id);}
        System.out.println(index);
        return index;
    }

    @Override
    public int updateSchedule(Long id, ScheduleRequestDto dto) {
        User user= jdbcTemplate.query("select * from user where USER_ID = (select USER_ID from schedule where USER_ID= ?)", userRowMapper(), id).get(0);
        if (!Objects.equals(dto.getPassword(), user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}

        Date now= new Date();
        int index=0;
        if (dto.getPlan() != null){index=jdbcTemplate.update("update schedule set PLAN= ? , EDITED_DATE = ? where id= ?", dto.getPlan(),now, id);}
        if (dto.getUserId() != null){index=jdbcTemplate.update("update schedule set USER_ID= ? , EDITED_DATE = ? where id= ?", dto.getUserId(),now, id);}
        return index;
    }

    @Override
    public int deleteUser(Long id, UserRequestDto dto) {
        String schedulePassword= jdbcTemplate.query("select * from user where USER_ID = ?", userRowMapper(), id).get(0).getPassword();
        if (!schedulePassword.equals(dto.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
        return jdbcTemplate.update("delete from user where USER_ID= ?", id);
    }

    @Override
    public int deleteSchedule(Long id, ScheduleRequestDto dto) {
        String schedulePassword= jdbcTemplate.query("select * from user where USER_ID = (SELECT USER_ID from schedule where ID = ?)", userRowMapper(), id).get(0).getPassword();
        if (!schedulePassword.equals(dto.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
        return jdbcTemplate.update("delete from schedule where id= ?", id);
    }

    public RowMapper<User> userRowMapper(){
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(rs.getLong("USER_ID"), rs.getString("USER_NAME"), rs.getString("USER_MAIL"), rs.getDate("REGISTED_DATE"), rs.getDate("EDITED_DATE"), rs.getString("PASSWORD"));
            }
        };
    }

    public RowMapper<Schedule> scheduleRowMapper(){
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(rs.getLong("ID"), rs.getLong("USER_ID"), rs.getString("PLAN"), rs.getDate("CREATED_DATE"), rs.getDate("EDITED_DATE"));
            }
        };
    }
}

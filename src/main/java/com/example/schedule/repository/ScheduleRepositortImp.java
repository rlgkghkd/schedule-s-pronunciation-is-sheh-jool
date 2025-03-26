package com.example.schedule.repository;

import com.example.schedule.dto.*;
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

    // JdbcTemplate 싱글톤
    private final JdbcTemplate jdbcTemplate;
    public ScheduleRepositortImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 유저 생성
    // 생성된 데이터는 user테이블에 삽입됨.
    // dto에서 받은 이름, 메일, 비밀번호를 사용
    // userId는 자동생성, 날짜는 현재 날짜 메서드 사용
    // UserResponseDto 객체를 만들어 반환
    @Override
    public UserResponseDto createUser(UserCreateRequestDto dto) {
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

    // 스케줄 생성
    // 생성된 데이터는 schedule 테이블에 삽입됨
    // userId, plan을 dto에서 받아 사용. userId는 user테이블의 USER_ID 외래키. user테이블에 없는 값은 sql 에러 발생
    // id는 자동생성, 날짜는 현재날짜 메서드
    // ScheduleResponseDto 객체를 반환. 반환시 유저 이름은 user테이블에서 USER_NAME 참조.
    @Override
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto dto) {

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

    // 유저 검색
    // 들어오는 파라미터에 따라 sql 쿼리 문자열을 만들어나감
    // 완성된 쿼리를 jdbcTemplate 쿼리 메서드에 전달.
    // 완성된 리스트를 dto 방식으로 매핑해 전달
    @Override
    public List<UserResponseDto> getUser(Long userId, String userName) {
        String sqlQuery="select * from user where 1=1";
        sqlQuery= userId==null?sqlQuery+" and 1 is Not ?": sqlQuery+" and USER_ID = ?";
        sqlQuery= userName==null?sqlQuery+" and 1 is Not ?": sqlQuery+" and USER_NAME = ?";
        System.out.println(sqlQuery);
        List<User> userList= jdbcTemplate.query(sqlQuery, userRowMapper(), userId, userName);
        if (userList.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        return userList.stream().map(UserResponseDto::new).toList();
    }


    // 스케줄 검색
    // 들어오는 파라미터에 따라 sql 쿼리 문자열을 만들어나감
    // 완성된 쿼리를 jdbcTemplate 쿼리 메서드에 전달.
    // 완성된 리스트와 user 테이블에서 USER_NAME을 참조하여 ScheduleResponseDto 방식으로 매핑
    // 완성된 dto 리스트를 전달.
    @Override
    public List<ScheduleResponseDto> getSchedule(Long id, String plan, Long userId, Date createdDate, Date editedDate) {
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

        List<ScheduleResponseDto> returnList = new ArrayList<>();
        List<Schedule> scheduleList= jdbcTemplate.query(sqlQuery, scheduleRowMapper(), id, plan, userId, created, edited);
        if (scheduleList.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        for (Schedule s: scheduleList){
            returnList.add(new ScheduleResponseDto(s, jdbcTemplate.query("select * from user where USER_ID = ?", userRowMapper(), s.getUserId()).get(0).getUserName()));
        }
        return returnList;
    }


    // 유저 업데이트
    // 비밀번호를 검사 후 일치할 경우에만 작업 수행
    // 들어오는 파라미터에 따라 sql 쿼리 문자열을 만들어나감
    // 완성된 쿼리를 jdbcTemplate 쿼리 메서드에 전달.
    // 수정 완료후 수정된 수 만큼 index에 저장.
    // index값 리턴.
    @Override
    public int updateUser(Long id, UserUpdateRequestDto dto) {
        User user= jdbcTemplate.query("select * from user where USER_ID = ?", userRowMapper(), id).get(0);
        if (!Objects.equals(dto.getPassword(), user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}

        Date now= new Date();
        int index=0;
        if (dto.getUserName() != null){index=jdbcTemplate.update("update user set USER_NAME= ? , EDITED_DATE = ? where USER_ID= ?", dto.getUserName(),now, id);}
        if (dto.getUserMail() != null){index=jdbcTemplate.update("update user set USER_MAIL= ? , EDITED_DATE = ? where USER_ID= ?", dto.getUserMail(),now, id);}
        return index;
    }
    
    // 스케줄 업데이트
    // 유저 업데이트와 같은 동작
    @Override
    public int updateSchedule(Long id, ScheduleUpdateRequestDto dto) {
        User user= jdbcTemplate.query("select * from user where USER_ID = (select USER_ID from schedule where ID= ?)", userRowMapper(), id).get(0);
        if (!Objects.equals(dto.getPassword(), user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}

        Date now= new Date();
        int index=0;
        if (dto.getPlan() != null){index=jdbcTemplate.update("update schedule set PLAN= ? , EDITED_DATE = ? where id= ?", dto.getPlan(),now, id);}
        if (dto.getUserId() != null){index=jdbcTemplate.update("update schedule set USER_ID= ? , EDITED_DATE = ? where id= ?", dto.getUserId(),now, id);}
        return index;
    }

    // 유저 삭제
    // 비밀번호를 검사 후 일치할 경우에만 작업 수행
    // 받은 userId 값에 해당하는 user를 테이블에서 찾아 삭제
    // 반환값은 int. 삭제된 행의 숫자를 전달함.
    @Override
    public int deleteUser(Long id, UserCreateRequestDto dto) {
        String schedulePassword= jdbcTemplate.query("select * from user where USER_ID = ?", userRowMapper(), id).get(0).getPassword();
        if (!schedulePassword.equals(dto.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
        return jdbcTemplate.update("delete from user where USER_ID= ?", id);
    }

    // 스케줄 삭제
    // 비밀번호는 user 테이블을 참조함
    // 나머지 동작은 유저 삭제와 동일
    @Override
    public int deleteSchedule(Long id, ScheduleCreateRequestDto dto) {
        String schedulePassword= jdbcTemplate.query("select * from user where USER_ID = (SELECT USER_ID from schedule where ID = ?)", userRowMapper(), id).get(0).getPassword();
        if (!schedulePassword.equals(dto.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
        return jdbcTemplate.update("delete from schedule where id= ?", id);
    }

    // 유저 sql 결과값을 RowMapper로 변환해 받음
    public RowMapper<User> userRowMapper(){
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(rs.getLong("USER_ID"), rs.getString("USER_NAME"), rs.getString("USER_MAIL"), rs.getDate("REGISTED_DATE"), rs.getDate("EDITED_DATE"), rs.getString("PASSWORD"));
            }
        };
    }

    // 스케줄 sql 결과값을 RowMapper로 변환해 받음
    public RowMapper<Schedule> scheduleRowMapper(){
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(rs.getLong("ID"), rs.getLong("USER_ID"), rs.getString("PLAN"), rs.getDate("CREATED_DATE"), rs.getDate("EDITED_DATE"));
            }
        };
    }
}

package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
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
import java.util.*;

@Slf4j
@Repository
public class ScheduleRepositortImp implements ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepositortImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto) {

        SimpleJdbcInsert simpleJdbcInsert= new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> param= new HashMap<>();
        Date now= new Date();

        param.put("todo", dto.getTodo());
        param.put("name", dto.getName());
        param.put("password", dto.getPassword());
        param.put("writeDate", now);
        param.put("rewriteDate", now);

        Number key= simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(param));

        return new ScheduleResponseDto(key.longValue(), dto.getTodo(), dto.getName(), now, now);
    }


    @Override
    public List<ScheduleResponseDto> getSchedule(Long id, String todo, String name, Date writeDate, Date rewriteDate) {

        String sqlQuery="select * from schedule where 1=1";
        sqlQuery= id==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and ID = ?";
        sqlQuery= todo==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and TODO = ?";
        sqlQuery= name==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and NAME = ?";
        sqlQuery= writeDate==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and WRITE_DATE = ?";
        sqlQuery= rewriteDate==null?sqlQuery+" and 1 is Not ?":sqlQuery+" and REWRITE_DATE = ?";

        List<Schedule> scheduleList= jdbcTemplate.query(sqlQuery, scheduleRowMapper(), id,todo, name, writeDate, rewriteDate);
        if (scheduleList.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
        return scheduleList.stream().map(ScheduleResponseDto::new).toList();
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {
        List<Schedule> searchedSchedule= jdbcTemplate.query("select * from schedule where id= ?", scheduleRowMapper(), id);
        ScheduleResponseDto searchedScheduleDto= new ScheduleResponseDto(searchedSchedule.stream().findAny().orElseThrow());
        if (!Objects.equals(dto.getPassword(), searchedScheduleDto.getId())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}

        if (dto.getTodo() != null){jdbcTemplate.update("update schedule set TODO= ?", dto.getTodo());}
        if (dto.getName() != null){jdbcTemplate.update("update schedule set NAME= ?", dto.getName());}
        return jdbcTemplate.update(sqlQuery);
    }

    public RowMapper<Schedule> scheduleRowMapper(){
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(rs.getLong("ID"), rs.getString("TODO"), rs.getString("NAME"), rs.getDate("WRITE_DATE"), rs.getDate("REWRITE_DATE"));
            }
        };
    }
}

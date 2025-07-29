package kr.co.wayplus.travel.mapper.common;

import kr.co.wayplus.travel.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface LoggerMapper {

    void insertLogMailSend(LogMailSend mailSend);


    void updateMailingLogResult(LogMailSend mailSend);

}

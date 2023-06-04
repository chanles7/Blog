package com.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.entity.UserLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
@Mapper
public interface UserLogMapper extends BaseMapper<UserLog> {

    @Select("select count(distinct ip) from b_user_log where DATE_FORMAT(create_time,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')")
    Integer getToday();

    List<Map<String,Object>> getUserAccess(String date);
}

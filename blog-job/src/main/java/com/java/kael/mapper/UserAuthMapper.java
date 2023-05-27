package com.java.kael.mapper;

import com.java.kael.entity.UserAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-12-25
 */
@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    void deleteByUserIds(@Param("ids") List<Integer> ids);

    UserAuth getByUserId(Object loginId);
}

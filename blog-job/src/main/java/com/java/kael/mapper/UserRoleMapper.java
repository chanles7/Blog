package com.java.kael.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.kael.entity.Menu;
import com.java.kael.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统管理 - 用户角色关联表  Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-30
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {


}

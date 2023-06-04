package com.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

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

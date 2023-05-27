package com.java.kael.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.kael.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 系统管理 - 角色-权限资源关联表  Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-30
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}

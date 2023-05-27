package com.java.kael.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.kael.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统管理-权限资源表  Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-30
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> getMenuByUserId(Object loginId);
}

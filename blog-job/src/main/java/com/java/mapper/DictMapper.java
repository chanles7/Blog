package com.java.mapper;

import com.java.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {

}

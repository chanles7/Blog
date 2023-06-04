package com.java.mapper;

import com.java.entity.Photo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 照片 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@Mapper
public interface PhotoMapper extends BaseMapper<Photo> {

    void movePhoto(@Param("idList") List<Integer> ids, @Param("albumId") Integer albumId);
}

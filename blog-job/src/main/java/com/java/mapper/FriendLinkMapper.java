package com.java.mapper;

import com.java.entity.FriendLink;
import com.java.vo.FriendLinkVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 友情链接表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Mapper
public interface FriendLinkMapper extends BaseMapper<FriendLink> {

    Integer getMaxSort();

    void top(@Param("id") Integer id, @Param("sort") int sort);

    List<FriendLinkVO> selectLinkList(@Param("status") Integer status);

}

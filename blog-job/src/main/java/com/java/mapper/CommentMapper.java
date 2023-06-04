package com.java.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.entity.Comment;
import com.java.vo.CommentVO;
import com.java.vo.ReplyCountVO;
import com.java.vo.ReplyVO;
import com.java.vo.SystemCommentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 博客评论表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-19
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentVO> listComments(@Param("page") int page, @Param("size") int size, @Param("articleId") Long articleId);

    List<ReplyVO> listReplies(Integer id);

    ReplyCountVO listReplyCountByCommentId(Integer id);

    Page<SystemCommentVO> selectPageList(@Param("page")Page<Object> objectPage, @Param("keywords")String keywords);
}

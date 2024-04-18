package com.dzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author DZY
 * @description 针对表【comment(用户评论表)】的数据库操作Mapper
 * @createDate 2024-04-18 10:23:55
 * @Entity generator.domain.Comment
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}





package com.dzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.model.entity.UserComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author DZY
 * @description 针对表【user_comment(用户评论表)】的数据库操作Mapper
 * @createDate 2024-04-03 12:06:10
 * @Entity generator.domain.UserComment
 */
@Mapper
public interface UserCommentMapper extends BaseMapper<UserComment> {

}





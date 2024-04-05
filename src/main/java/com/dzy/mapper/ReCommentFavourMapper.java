package com.dzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.model.entity.ReCommentFavour;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author DZY
 * @description 针对表【re_comment_favour(评论点赞关联表(硬删除))】的数据库操作Mapper
 * @createDate 2024-04-05 11:25:09
 * @Entity generator.domain.ReCommentFavour
 */
@Mapper
public interface ReCommentFavourMapper extends BaseMapper<ReCommentFavour> {

}





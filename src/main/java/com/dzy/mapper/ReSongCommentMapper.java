package com.dzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.model.entity.ReSongComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author DZY
 * @description 针对表【re_song_comment(歌曲用户评论关联表)】的数据库操作Mapper
 * @createDate 2024-04-15 22:14:00
 * @Entity generator.domain.ReSongComment
 */
@Mapper
public interface ReSongCommentMapper extends BaseMapper<ReSongComment> {

}





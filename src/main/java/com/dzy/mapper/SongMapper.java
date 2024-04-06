package com.dzy.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.model.entity.Comment;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.comment.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author DZY
 * @description 针对表【song(歌曲表)】的数据库操作Mapper
 * @createDate 2024-04-05 13:41:01
 * @Entity generator.domain.Song
 */
@Mapper
public interface SongMapper extends BaseMapper<Song> {

    Page<CommentVO> listCommentByPage(IPage<Comment> page, @Param(Constants.WRAPPER) Wrapper<Comment> queryWrapper);

//    Page<CommentVO> listCommentByPage(IPage<Comment> page,
//                                      @Param(Constants.WRAPPER) Wrapper<Comment> queryWrapper,
//                                      @Param("commentIdList") List<ReSongComment> comcommentIdList);

}





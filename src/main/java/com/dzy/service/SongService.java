package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.song.SongCommentQueryRequest;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.comment.CommentVO;

/**
 * @author DZY
 * @description 针对表【song(歌曲表)】的数据库操作Service
 * @createDate 2024-04-03 11:40:52
 */
public interface SongService extends IService<Song> {

    /**
     * 分页查询歌曲的评论
     *
     * @param songCommentQueryRequest
     * @return
     */
    Page<CommentVO> listSongCommentByPage(SongCommentQueryRequest songCommentQueryRequest);
}

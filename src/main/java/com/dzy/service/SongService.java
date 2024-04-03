package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.song.ReplyCreateRequest;
import com.dzy.model.dto.song.SongCommentCreateRequest;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.userinfo.UserLoginVO;

/**
 * @author DZY
 * @description 针对表【song(歌曲表)】的数据库操作Service
 * @createDate 2024-04-03 11:40:52
 */
public interface SongService extends IService<Song> {

    /**
     * 创建歌曲评论
     *
     * @param songCommentCreateRequest
     * @param loginUserVO
     * @return
     */
    Boolean createComment(SongCommentCreateRequest songCommentCreateRequest, UserLoginVO loginUserVO);

    /**
     * 创建歌曲评论回复
     *
     * @param replyCreateRequest
     * @param loginUserVO
     * @return
     */
    Boolean createReply(ReplyCreateRequest replyCreateRequest, UserLoginVO loginUserVO);

}

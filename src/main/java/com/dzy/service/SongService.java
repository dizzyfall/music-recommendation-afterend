package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.song.SongCommentCreateRequest;
import com.dzy.model.dto.song.SongCommentQueryRequest;
import com.dzy.model.dto.song.SongDetailQueryRequest;
import com.dzy.model.dto.song.SongQueryRequest;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.song.SongDetailVO;
import com.dzy.model.vo.song.SongIntroVO;

/**
 * @author DZY
 * @description 针对表【song(歌曲表)】的数据库操作Service
 * @createDate 2024-04-03 11:40:52
 */
public interface SongService extends IService<Song> {

    /**
     * Song转SongDetailVO
     * 不能简单使用属性复制，因为SongVO还有Song中没有的属性
     *
     * @param song
     * @return
     */
    SongDetailVO getSongDetailVO(Song song);

    /**
     * 获取歌曲详情
     *
     * @param songId
     * @return
     */
    SongDetailVO getSongDetailVOById(Long songId);

    /**
     * 获取歌曲简介
     *
     * @param song
     * @return
     */
    SongIntroVO getSongIntroVO(Song song);

    /**
     * 通过歌曲Id获取歌曲简介
     *
     * @param songId
     * @return
     */
    SongIntroVO getSongIntroVOById(Long songId);

    /**
     * 创建歌曲评论
     *
     * @param songCommentCreateRequest
     * @return
     */
    Boolean createComment(SongCommentCreateRequest songCommentCreateRequest);

    /**
     * 分页查询歌曲的评论
     *
     * @param songCommentQueryRequest
     * @return
     */
    Page<CommentVO> listSongCommentByPage(SongCommentQueryRequest songCommentQueryRequest);

    /**
     * 分页查询歌手歌曲
     *
     * @param songQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.song.SongIntroVO>
     * @date 2024/4/30  23:19
     */
    Page<SongIntroVO> listSongByPage(SongQueryRequest songQueryRequest);

    /**
     * 查询指定歌曲详情
     *
     * @param songDetailQueryRequest
     * @return com.dzy.model.vo.song.SongDetailVO
     * @date 2024/5/18  12:18
     */
    SongDetailVO searchSongDetail(SongDetailQueryRequest songDetailQueryRequest);

}

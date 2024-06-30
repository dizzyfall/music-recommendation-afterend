package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.songlist.*;
import com.dzy.model.entity.Songlist;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.model.vo.songlist.SonglistDetailVO;
import com.dzy.model.vo.songlist.SonglistIntroVO;

import java.util.List;

/**
 * @author DZY
 * @description 针对表【songlist(歌单表)】的数据库操作Service
 * @createDate 2024-04-11 19:40:21
 */
public interface SonglistService extends IService<Songlist> {

    /**
     * 创建歌单
     *
     * @param songlistCreateRequest
     * @return
     */
    Boolean createSonglist(SonglistCreateRequest songlistCreateRequest);

    /**
     * 添加歌曲到歌单
     *
     * @param addSongRequest
     * @return
     */
    Boolean addSong(AddSongRequest addSongRequest);

    /**
     * 批量添加歌曲到歌单
     *
     * @param addBatchSongRequest
     * @return
     */
    Boolean addBatchSong(AddBatchSongRequest addBatchSongRequest);

    /**
     * 删除歌单
     *
     * @param songlistDeleteRequest
     * @return
     */
    Boolean deleteSonglist(SonglistDeleteRequest songlistDeleteRequest);

    /**
     * 删除歌单（批量删除）
     *
     * @param songlistDeleteBatchRequest
     * @return
     */
    Boolean deleteBatchSonglist(SonglistDeleteBatchRequest songlistDeleteBatchRequest);

    /**
     * 删除歌曲
     *
     * @param deleteSongRequest
     * @return
     */
    Boolean deleteSong(DeleteSongRequest deleteSongRequest);

    /**
     * 删除歌曲（批量删除）
     *
     * @param deleteBatchSongRequest
     * @return
     */
    Boolean deleteBatchSong(DeleteBatchSongRequest deleteBatchSongRequest);

    /**
     * 创建歌单评论
     *
     * @param songlistCommentCreateRequest
     * @return java.lang.Boolean
     * @date 2024/4/13  23:42
     */
    Boolean createComment(SonglistCommentCreateRequest songlistCommentCreateRequest);

    /**
     * 获取歌单详情视图
     *
     * @param songlist
     * @return com.dzy.model.vo.songlist.SonglistDetailVO
     * @date 2024/6/1  19:15
     */
    SonglistDetailVO getSonglistDetailVO(Songlist songlist);

    /**
     * 获取歌单简介视图
     *
     * @param songlist
     * @return com.dzy.model.vo.songlist.SonglistIntroVO
     * @date 2024/4/29  20:56
     */
    SonglistIntroVO getSonglistIntroVO(Songlist songlist);

    /**
     * 根据歌单id获取歌单简介视图
     *
     * @param songlistId
     * @return com.dzy.model.vo.songlist.SonglistIntroVO
     * @date 2024/4/14  11:14
     */
    SonglistIntroVO getSonglistIntroVOById(Long songlistId);

    /**
     * 分页查询歌单的评论
     *
     * @param songlistCommentQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.comment.CommentVO>
     * @date 2024/4/15  12:07
     */
    Page<CommentVO> listSonglistCommentByPage(SonglistCommentQueryRequest songlistCommentQueryRequest);

    /**
     * 分页
     * 按标签查询歌单（包含‘全部’标签）
     * 标签为固定标签
     *
     * @param songlistTagsQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.songlist.SonglistIntroVO>
     * @date 2024/4/29  20:14
     */
    Page<SonglistIntroVO> listSonglistByTagsByPage(SonglistTagsQueryRequest songlistTagsQueryRequest);

    /**
     * 查询指定歌单信息
     *
     * @param songlistDetailQueryRequest
     * @return com.dzy.model.vo.songlist.SonglistDetailVO
     * @date 2024/6/1  19:08
     */
    SonglistDetailVO searchSonglistDetail(SonglistDetailQueryRequest songlistDetailQueryRequest);

    /**
     * 查询指定歌单所有歌曲
     *
     * @param songlistDetailQueryRequest
     * @return java.util.List<com.dzy.model.vo.song.SongIntroVO>
     * @date 2024/6/1  22:42
     */
    List<SongIntroVO> listSong(SonglistDetailQueryRequest songlistDetailQueryRequest);

    /**
     * 查询自己创建的歌单
     *
     * @param songlistQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.songlist.SonglistIntroVO>
     * @date 2024/6/6  18:10
     */
    Page<SonglistIntroVO> listMyCreateSonglistByPage(SonglistQueryRequest songlistQueryRequest);

}

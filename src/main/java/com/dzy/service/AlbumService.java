package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.album.AlbumCommentCreateRequest;
import com.dzy.model.dto.album.AlbumCommentQueryRequest;
import com.dzy.model.dto.album.AlbumQueryRequest;
import com.dzy.model.dto.album.AlbumSongQueryRequest;
import com.dzy.model.entity.Album;
import com.dzy.model.vo.album.AlbumInfoVO;
import com.dzy.model.vo.album.AlbumVO;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.model.vo.song.SongIntroVO;

import java.util.List;

/**
 * @author DZY
 * @description 针对表【album(专辑表)】的数据库操作Service
 * @createDate 2024-04-07 13:26:21
 */
public interface AlbumService extends IService<Album> {

    /**
     * 分页查询歌手专辑
     *
     * @param albumQueryRequest
     * @return
     */
    Page<AlbumVO> listAlbumByPage(AlbumQueryRequest albumQueryRequest);

    /**
     * 获取专辑详情视图
     *
     * @param album
     * @return
     */
    AlbumInfoVO getAlbumInfoVO(Album album);

    /**
     * 查询指定专辑信息
     *
     * @param albumSongQueryRequest
     * @return
     */
    AlbumInfoVO searchAlbumInfo(AlbumSongQueryRequest albumSongQueryRequest);

    /**
     * 查询指定专辑所有歌曲
     *
     * @param albumSongQueryRequest
     * @return
     */
    List<SongIntroVO> listSong(AlbumSongQueryRequest albumSongQueryRequest);

    /**
     * 创建专辑评论
     *
     * @param albumCommentCreateRequest
     * @return
     */
    Boolean createComment(AlbumCommentCreateRequest albumCommentCreateRequest);

    /**
     * 分页查询专辑的评论
     *
     * @param albumCommentQueryRequest
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.dzy.model.vo.comment.CommentVO>
     * @date 2024/4/15  9:38
     */
    Page<CommentVO> listAlbumCommentByPage(AlbumCommentQueryRequest albumCommentQueryRequest);

}

package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.SongMapper;
import com.dzy.model.dto.song.SongCommentQueryRequest;
import com.dzy.model.entity.Comment;
import com.dzy.model.entity.ReSongComment;
import com.dzy.model.entity.Song;
import com.dzy.model.vo.comment.CommentVO;
import com.dzy.service.CommentService;
import com.dzy.service.ReSongCommentService;
import com.dzy.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author DZY
 * @description 针对表【song(歌曲表)】的数据库操作Service实现
 * @createDate 2024-04-03 11:40:52
 */
@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song>
        implements SongService {

    @Autowired
    private CommentService commentService;

    @Resource
    private SongMapper songMapper;

    @Autowired
    private ReSongCommentService reSongCommentService;

    /**
     * 分页查询歌曲的评论
     *
     * @param songCommentQueryRequest
     * @return
     */
    @Override
    public Page<CommentVO> listSongCommentByPage(SongCommentQueryRequest songCommentQueryRequest) {
        Long songId = songCommentQueryRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<ReSongComment> queryWrapper = new QueryWrapper<>();
        //只查询需要的字段
        //List<String> columnList = Arrays.asList("id","comment_id","create_user_id","receiver_id","replier_id");
        ////查询关联表数据
        //queryWrapper.eq("song_id",songId).select(columnList);
        queryWrapper.eq("song_id", songId).select("comment_id");
        //获取评论id列表
        List<ReSongComment> commentIdList = reSongCommentService.list(queryWrapper);
        //构造评论条件查询器
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        //commentQueryWrapper.in("id", commentIdList);
        //分页对象
        int pageCurrent = songCommentQueryRequest.getPageCurrent();
        int pageSize = songCommentQueryRequest.getPageSize();
        Page<Comment> page = new Page<>(pageCurrent, pageSize);
        Page<CommentVO> commentPage = songMapper.listCommentByPage(page, commentQueryWrapper);
        //脱敏
        //List<CommentVO> commentVOList = commentPage.getRecords().stream().map(comment -> commentService.commentToCommentVO(comment)).collect(Collectors.toList());
        List<CommentVO> commentVOList = commentPage.getRecords();
        Page<CommentVO> commentVOPage = new Page<>(pageCurrent, pageSize, commentPage.getTotal());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

}





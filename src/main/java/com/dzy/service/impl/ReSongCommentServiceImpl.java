package com.dzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.mapper.ReSongCommentMapper;
import com.dzy.model.entity.ReSongComment;
import com.dzy.service.ReSongCommentService;
import org.springframework.stereotype.Service;

/**
 * @author DZY
 * @description 针对表【re_song_comment(歌曲用户评论关联表)】的数据库操作Service实现
 * @createDate 2024-04-03 10:43:39
 */
@Service
public class ReSongCommentServiceImpl extends ServiceImpl<ReSongCommentMapper, ReSongComment>
        implements ReSongCommentService {

}





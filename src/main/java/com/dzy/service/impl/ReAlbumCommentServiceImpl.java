package com.dzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.mapper.ReAlbumCommentMapper;
import com.dzy.model.entity.ReAlbumComment;
import com.dzy.service.ReAlbumCommentService;
import org.springframework.stereotype.Service;

/**
 * @author DZY
 * @description 针对表【re_album_comment(专辑评论关联表)】的数据库操作Service实现
 * @createDate 2024-04-08 22:24:23
 */
@Service
public class ReAlbumCommentServiceImpl extends ServiceImpl<ReAlbumCommentMapper, ReAlbumComment>
        implements ReAlbumCommentService {

}





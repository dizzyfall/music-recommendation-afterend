package com.dzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.mapper.ReCommentFavourMapper;
import com.dzy.model.entity.ReCommentFavour;
import com.dzy.service.ReCommentFavourService;
import org.springframework.stereotype.Service;

/**
 * @author DZY
 * @description 针对表【re_comment_favour(评论点赞关联表(硬删除))】的数据库操作Service实现
 * @createDate 2024-04-03 17:31:34
 */
@Service
public class ReCommentFavourServiceImpl extends ServiceImpl<ReCommentFavourMapper, ReCommentFavour>
        implements ReCommentFavourService {

}





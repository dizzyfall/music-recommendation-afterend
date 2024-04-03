package com.dzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.mapper.UserCommentMapper;
import com.dzy.model.entity.UserComment;
import com.dzy.service.UserCommentService;
import org.springframework.stereotype.Service;

/**
 * @author DZY
 * @description 针对表【user_comment(用户评论表)】的数据库操作Service实现
 * @createDate 2024-04-03 12:06:10
 */
@Service
public class UserCommentServiceImpl extends ServiceImpl<UserCommentMapper, UserComment>
        implements UserCommentService {

}





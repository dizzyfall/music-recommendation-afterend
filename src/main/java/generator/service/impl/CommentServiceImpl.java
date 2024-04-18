package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.mapper.CommentMapper;
import com.dzy.model.entity.Comment;
import generator.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * @author DZY
 * @description 针对表【comment(用户评论表)】的数据库操作Service实现
 * @createDate 2024-04-18 10:23:55
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

}





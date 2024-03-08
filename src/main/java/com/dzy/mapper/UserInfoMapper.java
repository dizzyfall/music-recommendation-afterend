package com.dzy.mapper;

import com.dzy.model.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @description 针对表【user_info(用户表)】的数据库操作Mapper
 * @createDate 2024-03-05 23:37:24
 * @Entity generator.domain.UserInfo
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}





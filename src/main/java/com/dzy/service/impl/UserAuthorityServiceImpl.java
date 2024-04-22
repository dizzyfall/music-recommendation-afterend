package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.mapper.UserAuthorityMapper;
import com.dzy.model.entity.UserAuthority;
import com.dzy.service.UserAuthorityService;
import com.dzy.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author DZY
 * @description 针对表【user_authority(用户权限表)】的数据库操作Service实现
 * @createDate 2024-04-21 17:02:37
 */
@Service
public class UserAuthorityServiceImpl extends ServiceImpl<UserAuthorityMapper, UserAuthority>
        implements UserAuthorityService {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 根据用户Id获取用户权限
     *
     * @param userId
     * @return com.dzy.model.entity.UserAuthority
     * @date 2024/4/21  17:06
     */
    @Override
    public UserAuthority getUserAuthority(Long userId) {
        QueryWrapper<UserAuthority> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.getOne(queryWrapper);
    }

}





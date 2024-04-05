package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.mapper.UserAuthorityMapper;
import com.dzy.model.entity.UserAuthority;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.UserAuthorityService;
import com.dzy.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.dzy.constant.UserInfoConstant.ADMIN_ROLE;

/**
 * 用户权限接口实现类
 *
 * @author DZY
 * @description 针对表【user_authority(用户权限表)】的数据库操作Service实现
 * @createDate 2024-03-08 21:06:03
 */
@Service
public class UserAuthorityServiceImpl extends ServiceImpl<UserAuthorityMapper, UserAuthority>
        implements UserAuthorityService {

    @Resource
    UserInfoService userInfoService;

    /**
     * 用户是否为管理员
     *
     * @param request 请求域
     * @return boolean
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        UserLoginVO userInfoLoginState = userInfoService.getUserInfoLoginState(request);
        Long userId = userInfoLoginState.getId();
        return isAdmin(userId);
    }

    /**
     * 用户是否为管理员
     *
     * @param id 用户id
     * @return boolean
     */
    @Override
    public boolean isAdmin(Long id) {
        QueryWrapper<UserAuthority> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id");
        UserAuthority admin = this.getOne(queryWrapper);
        Integer isAdmin = admin.getIsAdmin();
        return ADMIN_ROLE.equals(isAdmin);
    }
}





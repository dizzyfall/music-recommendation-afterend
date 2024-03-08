package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.userinfo.UserLoginRequest;
import com.dzy.model.dto.userinfo.UserRegisterRequest;
import com.dzy.model.dto.userinfo.UserUpdateRequest;
import com.dzy.model.entity.UserInfo;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.UserInfoService;
import com.dzy.mapper.UserInfoMapper;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dzy.constant.UserInfoConstant.SALT;
import static com.dzy.constant.UserInfoConstant.USER_LOGIN_STATE;

/**
 * 用户接口实现类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @description 针对表【user_info(用户表)】的数据库操作Service实现
 * @createDate 2024-03-05 23:37:24
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册请求的参数
     * @return 是否加入数据库
     */
    @Override
    public Boolean registerUser(UserRegisterRequest userRegisterRequest) {
        String account = userRegisterRequest.getAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //校验
        //账号、密码、确认密码是否为空、空字符串、空格
        if (StringUtils.isAnyBlank(account, password, checkPassword)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "账号、密码或确认密码为空、空字符串或空格");
        }
        //账号长度是否合法
        if (account.length() < 4 || account.length() > 20) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号长度至少4个字符且不超过20个字符");
        }
        //密码长度是否合法
        if (password.length() < 8 || password.length() > 20) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码长度需要8-20个字符");
        }
        //确认密码长度是否合法
        if (checkPassword.length() < 8 || checkPassword.length() > 20) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "确认密码长度需要8-20个字符");
        }
        //账号是否重复
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_info_account", account);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号已被注册");
        }
        if (count < 0) {
            throw new BusinessException(StatusCode.DATABASE_ERROR);
        }
        //账号是否合法
        //只能包括中文、英文字母、下划线、非空白字符
        String validPattern = "[\\u4E00-\\u9FA5A-Za-z0-9_\\S]{4,20}";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);
        if (!matcher.matches()) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号存在非法字符");
        }
        //密码是否合法
        //只能包括英文、数字、下划线、常用特殊字符、非空白字符
        if (!password.matches("[A-Za-z0-9_~!@#$%^&*()+\\S]{8,20}")) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码存在非法字符");
        }
        //密码和确认密码是否相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //用户数据加入数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPassword(encryptPassword);
        boolean insertResult = this.save(userInfo);
        if (!insertResult) {
            throw new BusinessException(StatusCode.DATABASE_ERROR, "用户注册数据没有加入数据库");
        }
        // TODO: 2024/3/8 返回的可以是用用户id,且可以将查询用户id方法封装
        return insertResult;
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求的参数
     * @param request 请求域
     * @return Boolean
     */
    @Override
    public UserLoginVO loginUser(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String account = userLoginRequest.getAccount();
        String password = userLoginRequest.getPassword();
        //账户是否非空、空字符串、含有空格
        if (StringUtils.isBlank(account)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "账户为空、空字符串或空格");
        }
        //密码是否非空、空字符串、含有空格
        if (StringUtils.isBlank(password)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "密码为空、空字符串或空格");
        }
        //账号长度是否合法
        if (account.length() < 4 || account.length() > 20) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号长度至少4个字符且不超过20个字符");
        }
        //密码长度是否合法
        if (password.length() < 8 || password.length() > 20) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码长度需要8-20个字符");
        }
        //账号是否合法
        String validPattern = "[\\u4E00-\\u9FA5A-Za-z0-9_\\S]{4,20}";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);
        if (!matcher.matches()) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号存在非法字符");
        }
        //密码是否合法
        if (!password.matches("[A-Za-z0-9_~!@#$%^&*()+\\S]{8,20}")) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码存在非法字符");
        }
        //账号和密码是否正确
        //查询没有被逻辑删除的数据，mybatis-plus配置逻辑删除！！！
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_info_account", account);
        queryWrapper.eq("user_info_password", encryptPassword);
        UserInfo userInfo = this.getOne(queryWrapper);
        if (userInfo == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号不存在或密码错误");
        }
        //用户脱敏
        UserLoginVO userLoginVO = userInfoToUserLoginVO(userInfo);
        //记录用户登录态
        setUserInfoLoginState(userLoginVO, request);
        return userLoginVO;
    }

    /**
     * 获取用户登录态
     *
     * @param request 请求域
     * @return UserInfo
     */
    @Override
    public UserInfo getUserInfoLoginState(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "请求域为空");
        }
        Object userInfoObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userInfoObj == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        return (UserInfo) userInfoObj;
    }

    /**
     * 设置用户登录态
     *
     * @param userLoginVO 用户脱敏信息
     */
    @Override
    public void setUserInfoLoginState(UserLoginVO userLoginVO, HttpServletRequest request) {
        if (userLoginVO == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "请求域为空");
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, userLoginVO);
    }

    /**
     * 移除用户登录态
     *
     * @param request 请求域
     */
    @Override
    public void removeUserInfoLoginState(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "请求域为空");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }

    /**
     * 用户信息脱敏，将userInfo转为UserLoginVO
     *
     * @param userInfo 用户信息
     * @return UserLoginVO
     */
    @Override
    public UserLoginVO userInfoToUserLoginVO(UserInfo userInfo) {
        if (userInfo == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(userInfo, userLoginVO);
        return userLoginVO;
    }

    /**
     * 用户退出登录
     *
     * @param request 请求域
     * @return Boolean
     */
    @Override
    public Boolean logoutUser(HttpServletRequest request) {
        removeUserInfoLoginState(request);
        Object userInfoLoginState = request.getSession().getAttribute(USER_LOGIN_STATE);
        return userInfoLoginState == null;
    }

    /**
     * 用户更新信息
     *
     * @param userUpdateRequest 更新请求的参数
     * @param request 请求域
     * @return Boolean
     */
    @Override
    public boolean updateUserInfo(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        return true;
    }
}





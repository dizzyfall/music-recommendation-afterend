package com.dzy.service;

import com.dzy.model.dto.userinfo.*;
import com.dzy.model.entity.UserImage;
import com.dzy.model.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.vo.userinfo.UserLoginVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @description 针对表【user_info(用户表)】的数据库操作Service
 * @createDate 2024-03-05 23:37:24
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册请求的参数
     * @return Boolean
     */
    Boolean registerUser(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求的参数
     * @param request          请求域
     * @return Boolean
     */
    UserLoginVO loginUser(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取用户登录态
     *
     * @param request 请求域
     * @return UserLoginVO
     */
    UserLoginVO getUserInfoLoginState(HttpServletRequest request);

    /**
     * 设置用户登录态
     *
     * @param userLoginVO 用户脱敏信息
     * @return Boolean
     */
    Boolean setUserInfoLoginState(UserLoginVO userLoginVO, HttpServletRequest request);

    /**
     * 移除用户登录态
     *
     * @param request 请求域
     * @return Boolean
     */
    Boolean removeUserInfoLoginState(HttpServletRequest request);

    /**
     * 用户信息脱敏，将userInfo转为UserLoginVO
     *
     * @param userInfo 用户信息
     * @return UserLoginVO
     */
    UserLoginVO userInfoToUserLoginVO(UserInfo userInfo);

    /**
     * 用户退出登录
     *
     * @param request 请求域
     * @return Boolean
     */
    Boolean logoutUser(HttpServletRequest request);

    /**
     * 用户更新信息
     *
     * @param userUpdateInfoRequest 更新请求的参数
     * @param request           请求域
     * @return Boolean
     */
    Boolean updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest, HttpServletRequest request);

    /**
     * 用户修改密码
     *
     * @param userUpdatePasswordRequest 密码更新的请求参数
     * @param request                   请求域
     * @return Boolean
     */
    Boolean updateUserInfoPassword(UserUpdatePasswordRequest userUpdatePasswordRequest, HttpServletRequest request);

    /**
     * 用户更新图片
     *
     * @param multipartFile
     * @param userUpdateImageRequest
     * @param loginUserVO
     * @return
     */
    Boolean updateUserImageByType(MultipartFile multipartFile, UserUpdateImageRequest userUpdateImageRequest, UserLoginVO loginUserVO);
}

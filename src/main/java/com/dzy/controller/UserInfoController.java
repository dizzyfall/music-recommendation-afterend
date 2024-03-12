package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.userinfo.*;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户基本功能控制层
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/5  23:44
 */
@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册请求的参数
     * @return Boolean
     */
    @PostMapping("/register")
    public BaseResponse<Boolean> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "注册请求参数为空");
        }
        Boolean isUserRegister = userInfoService.registerUser(userRegisterRequest);
        if (!isUserRegister) {
            throw new BusinessException(StatusCode.REGISTER_ERROR, "注册失败");
        }
        return ResponseUtil.success(StatusCode.REGISTER_SUCCESS);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求的参数
     * @param request          请求域
     * @return UserLoginVO
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "登录请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserLoginVO userLoginVO = userInfoService.loginUser(userLoginRequest, request);
        return ResponseUtil.success(StatusCode.LOGIN_SUCESS, userLoginVO);
    }

    /**
     * 用户退出登录
     *
     * @param request 请求域
     * @return Boolean
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        Boolean isUserLogout = userInfoService.logoutUser(request);
        if (!isUserLogout) {
            throw new BusinessException(StatusCode.STATE_DELETE_ERROR, "浏览器没有移除用户登录态错误");
        }
        return ResponseUtil.success(StatusCode.LOGOUT_SUCESS);
    }

    /**
     * 用户更新信息
     *
     * @param userUpdateInfoRequest 更新请求的参数
     * @param request           请求域
     * @return Boolean
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> userInfoUpdate(@RequestBody UserUpdateInfoRequest userUpdateInfoRequest, HttpServletRequest request) {
        if (userUpdateInfoRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "更新请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserLoginVO userInfoLoginState = userInfoService.getUserInfoLoginState(request);
        if (userInfoLoginState == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        Long loginUserId = userInfoLoginState.getId();
        Long requestId = userUpdateInfoRequest.getId();
        if (!loginUserId.equals(requestId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isUserUpdate = userInfoService.updateUserInfo(userUpdateInfoRequest, request);
        if (!isUserUpdate) {
            throw new BusinessException(StatusCode.UPDATE_ERROR, "用户更新信息失败");
        }
        return ResponseUtil.success(StatusCode.UPDATE_SUCESS);
    }

    /**
     * 用户更新密码
     *
     * @param userUpdatePasswordRequest 更新请求的参数
     * @param request                   请求域
     * @return Boolean
     */
    @PostMapping("/update/password")
    public BaseResponse<Boolean> userPasswordUpdate(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest, HttpServletRequest request) {
        if (userUpdatePasswordRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "更新请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserLoginVO userInfoLoginState = userInfoService.getUserInfoLoginState(request);
        if (userInfoLoginState == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        Long loginUserId = userInfoLoginState.getId();
        Long requestId = userUpdatePasswordRequest.getId();
        if (!loginUserId.equals(requestId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isUserUpdatePassword = userInfoService.updateUserInfoPassword(userUpdatePasswordRequest, request);
        if (!isUserUpdatePassword) {
            throw new BusinessException(StatusCode.UPDATE_ERROR, "用户更新密码失败");
        }
        return ResponseUtil.success(StatusCode.UPDATE_SUCESS);
    }

    /**
     * 用户更新图片
     *
     * @param multipartFile
     * @param userUpdateImageRequest
     * @param request
     * @return
     */
    @PostMapping("/update/image")
    public BaseResponse<Boolean> userImageUpdate(@RequestPart MultipartFile multipartFile, UserUpdateImageRequest userUpdateImageRequest, HttpServletRequest request) {
        if(multipartFile==null){
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        if (userUpdateImageRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "更新请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //是否登录
        UserLoginVO loginUserVO = userInfoService.getUserInfoLoginState(request);
        if(loginUserVO == null){
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        //是否是本人
        Long loginUserId = loginUserVO.getId();
        Long requestId = userUpdateImageRequest.getId();
        if (!loginUserId.equals(requestId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        //更新数据
        Boolean isUserUpdateImage = userInfoService.updateUserImageByType(multipartFile,userUpdateImageRequest,loginUserVO);
        if(!isUserUpdateImage){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"更新图片失败");
        }
        return ResponseUtil.success(StatusCode.UPDATE_SUCESS);
    }

}

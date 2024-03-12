package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.userinfo.UserUpdateImageRequest;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.UserImageService;
import com.dzy.service.UserInfoService;
import com.dzy.utils.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/12  9:48
 */
@RestController
@RequestMapping("/user/image")
public class UserImageController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserImageService userImageService;

    //@PostMapping("/upload")
    public BaseResponse<Boolean> uploadImage(@RequestPart MultipartFile multipartFile,
                                             UserUpdateImageRequest userUpdateImageRequest,
                                             HttpServletRequest request){
        if(multipartFile==null){
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        if(userUpdateImageRequest==null){
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        if(request==null){
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //是否登录
        UserLoginVO loginUserVO = userInfoService.getUserInfoLoginState(request);
        if(loginUserVO == null){
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        String imagePath = userImageService.uploadImageByType(multipartFile,userUpdateImageRequest,loginUserVO);
        if(StringUtils.isBlank(imagePath)){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"上传失败");
        }
        return ResponseUtil.success(StatusCode.CREATE_SUCESS);
    }

}

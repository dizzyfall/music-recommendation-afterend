package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.userinfo.UserUpdateImageRequest;
import com.dzy.model.entity.UserImage;
import com.dzy.model.vo.userinfo.UserLoginVO;
import org.springframework.web.multipart.MultipartFile;

/**
* @author DZY
* @description 针对表【user_image(用户图片表)】的数据库操作Service
* @createDate 2024-03-12 09:46:36
*/
public interface UserImageService extends IService<UserImage> {

    String uploadImageByType(MultipartFile multipartFile, UserUpdateImageRequest userUpdateImageRequest, UserLoginVO loginUserVO);

    String uploadImageByType(MultipartFile multipartFile, String type, UserLoginVO loginUserVO);

}

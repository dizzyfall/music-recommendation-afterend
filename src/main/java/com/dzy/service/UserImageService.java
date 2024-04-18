package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.userinfo.UserUpdateImageRequest;
import com.dzy.model.entity.UserImage;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DZY
 * @description 针对表【user_image(用户图片表)】的数据库操作Service
 * @createDate 2024-04-05 13:13:09
 */
public interface UserImageService extends IService<UserImage> {

    /**
     * 更新用户图片
     *
     * @param multipartFile
     * @param userUpdateImageRequest
     * @return java.lang.Boolean
     * @date 2024/4/18  23:43
     */
    Boolean updateUserImage(MultipartFile multipartFile, UserUpdateImageRequest userUpdateImageRequest);

    /**
     * 上传用户默认图像、背景
     *
     * @param userId
     * @return java.lang.Boolean
     * @date 2024/4/18  23:35
     */
    Boolean uploadDefaultImage(Long userId);

}

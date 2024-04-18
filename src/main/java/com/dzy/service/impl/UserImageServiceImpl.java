package com.dzy.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.UserImageMapper;
import com.dzy.mapper.UserInfoMapper;
import com.dzy.model.dto.userinfo.UserUpdateImageRequest;
import com.dzy.model.entity.UserImage;
import com.dzy.model.entity.UserInfo;
import com.dzy.model.enums.UserImageUploadEnum;
import com.dzy.service.UserImageService;
import com.dzy.service.UserInfoService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static com.dzy.constant.FileConstant.*;

/**
 * @author DZY
 * @description 针对表【user_image(用户图片表)】的数据库操作Service实现
 * @createDate 2024-04-05 13:13:09
 */
@Service
public class UserImageServiceImpl extends ServiceImpl<UserImageMapper, UserImage>
        implements UserImageService {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 更新用户图片
     *
     * @param multipartFile
     * @param userUpdateImageRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserImage(MultipartFile multipartFile, UserUpdateImageRequest userUpdateImageRequest) {
        if (userUpdateImageRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        String type = userUpdateImageRequest.getType();
        if (StringUtils.isBlank(type)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = userUpdateImageRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //是否需要删除本地图片文件
        QueryWrapper<UserImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserImage userImage = this.getOne(queryWrapper);
        if (userImage != null) {
            //删除本地图片文件
            Boolean isDeleteUserImageFile = deleteUserImageFile(userId, type);
            if (!isDeleteUserImageFile) {
                throw new BusinessException(StatusCode.DELETE_ERROR, "删除用户图片失败");
            }
        }
        //上传图片文件
        String imageName = uploadImageByType(multipartFile, type, userId);
        if (StringUtils.isBlank(imageName)) {
            throw new BusinessException(StatusCode.UPLOAD_IMAGE_ERROR);
        }
        //更新用户图片表数据
        updateImageToDB(type, imageName, userImage.getId(), userId);
        return true;
    }

    /**
     * 上传图片
     *
     * @param multipartFile
     * @return
     */
    //todo 使用对象存储
    public String uploadImageByType(MultipartFile multipartFile, String type, Long userId) {
        //获取上传图片类型
        UserImageUploadEnum userImageUploadEnum = UserImageUploadEnum.getEnumByValue(type);
        if (userImageUploadEnum == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户图片上传类型错误");
        }
        type = userImageUploadEnum.getValue();
        //校验图片
        validImage(multipartFile, userImageUploadEnum);
        //创建图片目录
        //todo 存入服务器后端项目中
        //todo linux文件分隔符是'/'
        String imageContent = String.format("%s\\%s\\%s", PROJECT_PATH, type, userId);
        File content = new File(imageContent);
        if (!content.isDirectory() && !content.exists()) {
            boolean isExistDir = content.mkdirs();
            if (!isExistDir) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建文件夹失败");
            }
        }
        //重写图片名称
        String imagePreffix = UUID.randomUUID().toString();
        String imageName = imagePreffix + "-" + multipartFile.getOriginalFilename();
        //拼接绝对路径
        String imagePath = String.format("%s\\%s", imageContent, imageName);
        //上传图片
        File image = new File(imagePath);
        try {
            //上传图片
            multipartFile.transferTo(image);
        } catch (Exception e) {
            log.error("image upload error, imagepath = " + imagePath, e);
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "上传失败");
        }
        return imageName;
    }

    /**
     * 校验图片
     *
     * @param multipartFile
     * @param userImageUploadEnum
     */
    public void validImage(MultipartFile multipartFile, UserImageUploadEnum userImageUploadEnum) {
        //图片大小
        long imageSize = multipartFile.getSize();
        //图片后缀
        String imageSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        //上传图片的类型
        if (UserImageUploadEnum.USER_AVATAR.equals(userImageUploadEnum)) {
            //大小限制
            if (imageSize > IMAGE_MAXSIZE) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "图片大小不能超过 5M");
            }
            //图片类型限制
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(imageSuffix)) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "图片类型错误，仅支持jpeg、jpg、svg、png和webp");
            }
        }
    }

    /**
     * 更新图片到数据库
     *
     * @param type
     * @param imageName
     * @param imageId
     * @param userId
     * @return java.lang.Boolean
     * @date 2024/4/18  15:03
     */
    public Boolean updateImageToDB(String type, String imageName, Long imageId, Long userId) {
        //设置图片信息
        UserImage userImage = new UserImage();
        userImage.setId(imageId);
        userImage.setUserId(userId);
        //如果是头像
        if (type.equals("user_avatar")) {
            //头像名称写入数据库
            userImage.setAvatarPath(imageName);
        }
        //如是背景
        if (type.equals("user_background")) {
            //背景名称写入数据库
            userImage.setBackgroundPath(imageName);
        }
        boolean isUpdateUserImage = this.updateById(userImage);
        if (!isUpdateUserImage) {
            throw new BusinessException(StatusCode.UPDATE_ERROR, "更新图片失败");
        }
        return true;
    }

    /**
     * 根据图片id和对应图片类型删除上传的图片文件
     *
     * @param userId
     * @param type
     * @return java.lang.Boolean
     * @date 2024/4/18  17:58
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserImageFile(Long userId, String type) {
        String imagePath = getImagePath(userId, type);
        File imageFile = new File(imagePath);
        boolean isImageFileDelete = imageFile.delete();
        if (!isImageFileDelete) {
            throw new BusinessException(StatusCode.DELETE_ERROR, "删除用户图片失败,图片类型为：" + type);
        }
        return true;
    }

    /**
     * 根据用户id和图片类型获取图片绝对路径
     *
     * @param userId
     * @param type
     * @return java.lang.String
     * @date 2024/4/18  20:00
     */
    public String getImagePath(Long userId, String type) {
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //获取图片id
        Long imageId = userInfoService.getById(userId).getImageId();
        UserImage userImage = this.getById(imageId);
        String imageName;
        if (type.equals("user_avatar")) {
            imageName = userImage.getAvatarPath();
        } else if (type.equals("user_background")) {
            imageName = userImage.getBackgroundPath();
        } else {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "图片类型错误");
        }
        String imageContent = String.format("%s\\%s\\%s", PROJECT_PATH, type, userId);
        return String.format("%s\\%s", imageContent, imageName);
    }

    /**
     * 上传默认头像
     *
     * @param userId
     * @return void
     * @date 2024/4/18  23:42
     */
    public void uploadDefaultAvatar(Long userId) {
        String imageContent = String.format("%s\\%s\\%s", PROJECT_PATH, "user_avatar", userId);
        File content = new File(imageContent);
        if (!content.isDirectory() && !content.exists()) {
            boolean isExistDir = content.mkdirs();
            if (!isExistDir) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建文件夹失败");
            }
        }
        //获取头像
        File defaultAvatar = new File(DEFAULT_AVATAR_ABSOLUTE_PATH);
        //上传头像
        File avatarContent = new File(imageContent);
        try {
            FileUtils.copyFileToDirectory(defaultAvatar, avatarContent);
        } catch (IOException e) {
            log.error("default avatar upload error", e);
            throw new BusinessException(StatusCode.UPLOAD_IMAGE_ERROR);
        }
    }

    /**
     * 上传默认背景
     *
     * @param userId
     * @return void
     * @date 2024/4/18  23:42
     */
    public void uploadDefaultBackground(Long userId) {
        String imageContent = String.format("%s\\%s\\%s", PROJECT_PATH, "user_background", userId);
        File content = new File(imageContent);
        if (!content.isDirectory() && !content.exists()) {
            boolean isExistDir = content.mkdirs();
            if (!isExistDir) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建文件夹失败");
            }
        }
        //获取头像
        File defaultBackground = new File(DEFAULT_BACKGROUND_ABSOLUTE_PATH);
        //上传头像
        File backgroundContent = new File(imageContent);
        try {
            FileUtils.copyFileToDirectory(defaultBackground, backgroundContent);
        } catch (IOException e) {
            log.error("default background upload error", e);
            throw new BusinessException(StatusCode.UPLOAD_IMAGE_ERROR);
        }
    }

    /**
     * 上传用户默认图像、背景
     *
     * @param userId
     * @return java.lang.Boolean
     * @date 2024/4/18  23:41
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean uploadDefaultImage(Long userId) {
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserInfo userInfo = userInfoService.getById(userId);
        if (userInfo == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        uploadDefaultAvatar(userId);
        uploadDefaultBackground(userId);
        UserImage userImage = new UserImage();
        userImage.setUserId(userId);
        userImage.setAvatarPath(DEFAULT_AVATAR_NAME);
        userImage.setBackgroundPath(DEFAULT_BACKGROUND_NAME);
        boolean isImageSave = this.save(userImage);
        if (!isImageSave) {
            throw new BusinessException(StatusCode.UPLOAD_IMAGE_ERROR);
        }
        Long imageId = userImage.getId();
        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId).set("image_id", imageId);
        boolean isUserInfoUpdate = userInfoService.update(updateWrapper);
        if (!isUserInfoUpdate) {
            throw new BusinessException(StatusCode.UPDATE_ERROR);
        }
        return true;
    }

}





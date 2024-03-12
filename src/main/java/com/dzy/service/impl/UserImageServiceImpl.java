package com.dzy.service.impl;

import java.util.*;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.UserInfoMapper;
import com.dzy.model.dto.userinfo.UserUpdateImageRequest;
import com.dzy.model.entity.UserImage;
import com.dzy.model.entity.UserInfo;
import com.dzy.model.enums.UserImageUploadEnum;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.UserImageService;
import com.dzy.mapper.UserImageMapper;
import com.dzy.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

import static com.dzy.constant.FileConstant.IMAGE_MAXSIZE;
import static com.dzy.constant.FileConstant.PROJECT_PATH;

/**
 * @author DZY
 * @description 针对表【user_image(用户图片表)】的数据库操作Service实现
 * @createDate 2024-03-12 09:46:36
 */
@Service
@Slf4j
public class UserImageServiceImpl extends ServiceImpl<UserImageMapper, UserImage>
        implements UserImageService {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 上传图片
     *
     * @param multipartFile
     * @param userUpdateImageRequest
     * @param loginUserVO
     * @return
     */
    //todo 使用对象对象存储
    //todo 返回可访问地址
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadImageByType(MultipartFile multipartFile, UserUpdateImageRequest userUpdateImageRequest, UserLoginVO loginUserVO) {
        String type = userUpdateImageRequest.getType();
        return uploadImageByType(multipartFile,type,loginUserVO);
    }

    /**
     * 上传图片
     * 重写
     *
     * @param multipartFile
     * @return
     */
    //todo 使用对象对象存储
    //todo 返回可访问地址
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadImageByType(MultipartFile multipartFile, String type ,UserLoginVO loginUserVO) {
        //获取上传图片类型
        UserImageUploadEnum userImageUploadEnum = UserImageUploadEnum.getEnumByValue(type);
        if (userImageUploadEnum == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        type = userImageUploadEnum.getValue();
        //校验图片
        validImage(multipartFile, userImageUploadEnum);
        //获取登录用户信息
        Long loginUserId = loginUserVO.getId();
        //创建图片目录
        //todo 存入服务器后端项目中
        //todo linux文件分隔符是'/'
        String imageContent = String.format("%s\\%s\\%s", PROJECT_PATH, type, loginUserId);
        File content = new File(imageContent);
        if (!content.isDirectory() && !content.exists()) {
            boolean isExistDir= content.mkdirs();
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
        return saveImage(type, imageName, loginUserId);
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
     * 将图片名称加入数据库
     *
     * @param type
     * @param imageName
     * @param loginUserId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String saveImage(String type, String imageName, Long loginUserId) {
        //根据用户Id获取数据
        QueryWrapper<UserImage> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("user_id",loginUserId);
        UserImage userImage = this.getOne(queryWrapper);
        //设置图片信息
        //如果没有加入用户图片
        if(ObjectUtils.isEmpty(userImage)) {
            //设置用户id
            userImage = new UserImage();
            userImage.setUserId(loginUserId);
        }
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
        boolean res = this.saveOrUpdate(userImage);
        if (!res) {
            throw new BusinessException(StatusCode.DATABASE_ERROR, "图片加入数据库错误");
        }
        //获取登录用户
        UserInfo oldUserInfo = userInfoMapper.selectById(loginUserId);
        UserInfo newUserInfo = new UserInfo();
        BeanUtils.copyProperties(oldUserInfo,newUserInfo);
        //获取新保存的用户图片
        UserImage newUserImage = this.getOne(queryWrapper);
        Long newUserImageId = newUserImage.getId();
        //设置user_info表的imageId字段
        newUserInfo.setImageId(newUserImageId);
        userInfoMapper.updateById(newUserInfo);
        return imageName;
    }

}





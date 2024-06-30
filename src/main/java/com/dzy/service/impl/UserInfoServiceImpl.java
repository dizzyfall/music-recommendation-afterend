package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.UserInfoMapper;
import com.dzy.model.dto.userinfo.*;
import com.dzy.model.entity.Collect;
import com.dzy.model.entity.UserAuthority;
import com.dzy.model.entity.UserImage;
import com.dzy.model.entity.UserInfo;
import com.dzy.model.vo.userinfo.UserInfoIntroVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.CollectService;
import com.dzy.service.UserAuthorityService;
import com.dzy.service.UserImageService;
import com.dzy.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserImageService userImageService;

    @Resource
    private UserAuthorityService userAuthorityService;

    @Resource
    private CollectService collectService;

    /**
     * 用户信息脱敏，将userInfo转为UserLoginVO
     *
     * @param userInfo 用户信息
     * @return UserLoginVO
     */
    @Override
    public UserLoginVO getUserLoginVO(UserInfo userInfo) {
        if (userInfo == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserLoginVO userLoginVO = UserLoginVO.objToVO(userInfo);
        //补充属性
        UserImage userImage = userImageService.getById(userInfo.getImageId());
        userLoginVO.setAvatarPath(userImage.getAvatarPath());
        UserAuthority userAuthority = userAuthorityService.getUserAuthority(userInfo.getId());
        userLoginVO.setUserRole(userAuthority.getUserRole());
        return userLoginVO;
    }

    /**
     * 获取用户信息简介
     *
     * @param userInfo
     * @return com.dzy.model.vo.userinfo.UserInfoIntroVO
     * @date 2024/4/15  11:05
     */
    @Override
    public UserInfoIntroVO getUserInfoIntroVO(UserInfo userInfo) {
        if (userInfo == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserInfoIntroVO userInfoIntroVO = UserInfoIntroVO.objToVO(userInfo);
        //补充属性
        UserImage userImage = userImageService.getById(userInfo.getImageId());
        userInfoIntroVO.setAvatarPath(userImage.getAvatarPath());
        userInfoIntroVO.setCreatorId(userInfo.getId());
        return userInfoIntroVO;
    }

    /**
     * 通过用户Id获取用户信息简介
     *
     * @param userId
     * @return com.dzy.model.vo.userinfo.UserInfoIntroVO
     * @date 2024/4/15  11:39
     */
    @Override
    public UserInfoIntroVO getUserInfoIntroVOById(Long userId) {
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserInfo userInfo = this.getById(userId);
        return getUserInfoIntroVO(userInfo);
    }

    /**
     * 获取用户登录态
     *
     * @param request 请求域
     * @return UserInfo
     */
    @Override
    public UserLoginVO getUserInfoLoginState(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "请求域为空");
        }
        Object userInfoObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userInfoObj == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        return (UserLoginVO) userInfoObj;
    }

    /**
     * 设置用户登录态
     *
     * @param userLoginVO 用户脱敏信息
     * @return Boolean
     */
    @Override
    public Boolean setUserInfoLoginState(UserLoginVO userLoginVO, HttpServletRequest request) {
        if (userLoginVO == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "请求域为空");
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, userLoginVO);
        return getUserInfoLoginState(request).equals(userLoginVO);
    }

    /**
     * 移除用户登录态
     *
     * @param request 请求域
     */
    @Override
    public Boolean removeUserInfoLoginState(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "请求域为空");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return request.getSession().getAttribute(USER_LOGIN_STATE) == null;
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册请求的参数
     * @return 是否加入数据库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
        queryWrapper.eq("account", account);
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
        //加入用户信息表
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPassword(encryptPassword);
        boolean isUserInfoSave = this.save(userInfo);
        if (!isUserInfoSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "用户注册数据没有加入数据库");
        }
        //获取用户id
        Long userId = userInfo.getId();
        //上传默认头像、背景
        Boolean isDefaultImageSave = userImageService.uploadDefaultImage(userId);
        if (!isDefaultImageSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "用户图像数据没有加入数据库");
        }
        //加入用户权限表
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUserId(userId);
        boolean isUserAuthoritySave = userAuthorityService.save(userAuthority);
        if (!isUserAuthoritySave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "用户权限数据没有加入数据库");
        }
        //初始化收藏表数据
        Collect collect = new Collect();
        collect.setUserId(userId);
        boolean isCollectSave = collectService.save(collect);
        if (!isCollectSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "用户收藏数据没有加入数据库");
        }
        //初始化用户评分表数据
//        UserRating userRating = new UserRating();
//        userRating.setUserId(userId);
//        boolean isUserRatingSave = userRatingService.save(userRating);
//        if (!isUserRatingSave) {
//            throw new BusinessException(StatusCode.CREATE_ERROR, "用户评分数据没有加入数据库");
//        }
        return true;
    }

    /**
     * 用户注册
     *
     * @param account
     * @param password
     * @param checkPassword
     * @return java.lang.Boolean
     * @date 2024/5/26  15:34
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean registerUser(String account, String password, String checkPassword) {
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
        queryWrapper.eq("account", account);
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
        //加入用户信息表
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPassword(encryptPassword);
        boolean isUserInfoSave = this.save(userInfo);
        if (!isUserInfoSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "用户注册数据没有加入数据库");
        }
        //获取用户id
        Long userId = userInfo.getId();
        //上传默认头像、背景
        Boolean isDefaultImageSave = userImageService.uploadDefaultImage(userId);
        if (!isDefaultImageSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "用户图像数据没有加入数据库");
        }
        //加入用户权限表
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUserId(userId);
        boolean isUserAuthoritySave = userAuthorityService.save(userAuthority);
        if (!isUserAuthoritySave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "用户权限数据没有加入数据库");
        }
        //初始化收藏表数据
        Collect collect = new Collect();
        collect.setUserId(userId);
        boolean isCollectSave = collectService.save(collect);
        if (!isCollectSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "用户收藏数据没有加入数据库");
        }
        //初始化用户评分表数据
//        UserRating userRating = new UserRating();
//        userRating.setUserId(userId);
//        boolean isUserRatingSave = userRatingService.save(userRating);
//        if (!isUserRatingSave) {
//            throw new BusinessException(StatusCode.CREATE_ERROR, "用户评分数据没有加入数据库");
//        }
        return true;
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求的参数
     * @param request          请求域
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
        queryWrapper.eq("account", account);
        queryWrapper.eq("password", encryptPassword);
        UserInfo userInfo = this.getOne(queryWrapper);
        if (userInfo == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号不存在或密码错误");
        }
        //用户脱敏
        UserLoginVO userLoginVO = getUserLoginVO(userInfo);
        //记录用户登录态
        setUserInfoLoginState(userLoginVO, request);
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
        return removeUserInfoLoginState(request);
    }

    /**
     * 用户更新信息
     *
     * @param userUpdateInfoRequest 更新请求的参数
     * @param request               请求域
     * @return Boolean
     */
    @Override
    public Boolean updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest, HttpServletRequest request) {
        //只有自己才能修改信息
        Long requestUserid = userUpdateInfoRequest.getId();
        UserLoginVO userInfoLoginState = getUserInfoLoginState(request);
        Long loginUserId = userInfoLoginState.getId();
        if (!requestUserid.equals(loginUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        //数据库查询用户信息
        UserInfo userInfo = this.getById(loginUserId);
        //新用户信息对象
        UserInfo newUserInfo = new UserInfo();
        BeanUtils.copyProperties(userInfo, newUserInfo);
        //是否修改昵称
        String newNickname = userUpdateInfoRequest.getNickname();
        String oldNickName = userInfo.getNickname();
        //新数据和原数据不相等才修改，否则不修改
        if (!newNickname.equals(oldNickName)) {
            newUserInfo.setNickname(newNickname);
        }
        //是否修改简介
        String newDescription = userUpdateInfoRequest.getDescription();
        String oldDescription = userInfo.getDescription();
        //新数据不为空且和原数据不相等才修改，否则不修改
        if (!newDescription.equals(oldDescription)) {
            newUserInfo.setDescription(newDescription);
        }
        //是否修改性别
        Integer newSex = userUpdateInfoRequest.getSex();
        Integer oldSex = userInfo.getSex();
        //新数据不为空且和原数据不相等才修改，否则不修改
        if (newSex != null && !newSex.equals(oldSex)) {
            newUserInfo.setSex(newSex);
        }
        //是否修改所属地
        String newRegion = userUpdateInfoRequest.getRegion();
        String oldRegion = userInfo.getRegion();
        //新数据不为空且和原数据不相等才修改，否则不修改
        if (StringUtils.isNotBlank(newRegion) && !newRegion.equals(oldRegion)) {
            newUserInfo.setRegion(newRegion);
        }
        //是否修改手机号码
        String newPhone = userUpdateInfoRequest.getPhone();
        String oldPhone = userInfo.getPhone();
        //新数据不为空且和原数据不相等才修改，否则不修改
        if (StringUtils.isNotBlank(newPhone) && !newPhone.equals(oldPhone)) {
            newUserInfo.setPhone(newPhone);
        }
        //是否修改邮箱
        String newEmail = userUpdateInfoRequest.getEmail();
        String oldEmail = userInfo.getEmail();
        //新数据不为空且和原数据不相等才修改，否则不修改
        if (StringUtils.isNotBlank(newEmail) && !newEmail.equals(oldEmail)) {
            newUserInfo.setEmail(newEmail);
        }
        //是否修改地址
        String newAddress = userUpdateInfoRequest.getAddress();
        String oldAddress = userInfo.getAddress();
        //新数据不为空且和原数据不相等才修改，否则不修改
        if (StringUtils.isNotBlank(newAddress) && !newAddress.equals(oldAddress)) {
            newUserInfo.setAddress(newAddress);
        }
        //是否修改生日
        Date newBirthday = userUpdateInfoRequest.getBirthday();
        Date oldBirthday = userInfo.getBirthday();
        //新数据不为空且和原数据不相等才修改，否则不修改
        if (newBirthday != null && !newBirthday.equals(oldBirthday)) {
            newUserInfo.setBirthday(newBirthday);
        }
        //更新用户信息
        int update = userInfoMapper.updateById(newUserInfo);
        if (update != 1) {
            throw new BusinessException(StatusCode.UPDATE_ERROR, "用户信息更新失败");
        }
        //获取更新后的用户信息
        UserInfo updateUserInfo = this.getById(loginUserId);
        //用户数据脱敏
        UserLoginVO newUserLoginVO = getUserLoginVO(updateUserInfo);
        //更新用户登录态
        return setUserInfoLoginState(newUserLoginVO, request);
    }

    /**
     * 用户修改密码
     *
     * @param userUpdatePasswordRequest 密码更新的请求参数
     * @param request                   请求域
     * @return Boolean
     */
    @Override
    public Boolean updateUserInfoPassword(UserUpdatePasswordRequest userUpdatePasswordRequest, HttpServletRequest request) {
        //只有自己才能修改信息
        Long requestUserid = userUpdatePasswordRequest.getId();
        UserLoginVO userInfoLoginState = getUserInfoLoginState(request);
        Long loginUserId = userInfoLoginState.getId();
        if (!requestUserid.equals(loginUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        //是否修改密码
        String newPassword = userUpdatePasswordRequest.getPassword();
        String newCheckPassword = userUpdatePasswordRequest.getCheckPassword();
        //密码和确认密码是否为空
        if (StringUtils.isAnyBlank(newPassword, newCheckPassword)) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "两次输入的密码不能为空");
        }
        //密码长度是否合法
        if (newPassword.length() < 8 || newPassword.length() > 20) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码长度需要8-20个字符");
        }
        //密码是否合法
        if (!newPassword.matches("[A-Za-z0-9_~!@#$%^&*()+\\S]{8,20}")) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码存在非法字符");
        }
        //密码和确认密码是否相同
        if (!newPassword.equals(newCheckPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        //加密新密码
        String newEncryptPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        //获取原加密密码
        UserInfo userInfo = this.getById(loginUserId);
        String oldEncryptPassword = userInfo.getPassword();
        //新数据和原数据不相等才修改，否则不修改
        if (newEncryptPassword.equals(oldEncryptPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "新密码和旧密码一致");
        }
        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("password", newEncryptPassword);
        updateWrapper.eq("id", loginUserId);
        //更新用户信息
        boolean isUpdate = this.update(updateWrapper);
        if (!isUpdate) {
            throw new BusinessException(StatusCode.UPDATE_ERROR, "用户密码更新失败");
        }
        //获取更新后的用户信息
        UserInfo newUserInfo = this.getById(loginUserId);
        //用户数据脱敏
        UserLoginVO newUserLoginVO = getUserLoginVO(newUserInfo);
        //更新用户登录态
        return setUserInfoLoginState(newUserLoginVO, request);
    }

    /**
     * 用户更新图片
     *
     * @param multipartFile
     * @param userUpdateImageRequest
     * @return
     */
    @Override
    public Boolean updateUserImageByType(MultipartFile multipartFile, UserUpdateImageRequest userUpdateImageRequest) {
        //获取更新图片类型
        String type = userUpdateImageRequest.getType();
        if (StringUtils.isBlank(type)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        //更新添加图片
        Boolean isUserImageUpdate = userImageService.updateUserImage(multipartFile, userUpdateImageRequest);
        if (!isUserImageUpdate) {
            throw new BusinessException(StatusCode.UPDATE_ERROR, "图片更新失败");
        }
        return true;
    }

    /**
     * 用户是否登录，用户信息是否一致
     *
     * @param loginUser
     * @param request
     * @return
     */
    public Boolean isLogin(UserLoginVO loginUser, HttpServletRequest request) {
        return null;
    }

}





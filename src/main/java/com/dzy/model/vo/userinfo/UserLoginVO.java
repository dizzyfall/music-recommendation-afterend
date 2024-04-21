package com.dzy.model.vo.userinfo;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.UserInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户返回登录信息视图（脱敏）
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/5  23:49
 */
@Data
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = -6338811984591369301L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像路径
     */
    private String avatarPath;

    /**
     * 用户权限
     */
    private Integer userRole;

    /**
     * 用户简介
     */
    private String description;

    /**
     * 用户创建时间
     */
    private Date createTime;

    /**
     * 用户更新时间
     */
    private Date updateTime;

    public static UserLoginVO objToVO(UserInfo userInfo) {
        if (userInfo == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        try {
            BeanUtils.copyProperties(userInfo, userLoginVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return userLoginVO;
    }

}

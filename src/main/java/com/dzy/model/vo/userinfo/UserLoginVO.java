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
     * 用户图片id
     */
    private Long imageId;

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
     * 用户性别 0:男 1:女 2:其他
     */
    private Integer sex;

    /**
     * 用户所属地
     */
    private String region;

    /**
     * 用户生日
     */
    private Date birthday;

    /**
     * 用户关注数量
     */
    private Integer attentionCount;

    /**
     * 用户粉丝数量
     */
    private Integer fanCount;

    /**
     * 用户好友数量
     */
    private Integer friendCount;

    /**
     * 用户访客数量
     */

    private Integer visitorCount;

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

package com.dzy.model.vo.userinfo;

import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.entity.UserInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/15  10:50
 */
@Data
public class UserInfoIntroVO implements Serializable {

    private static final long serialVersionUID = 6571961979854752127L;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像路径
     */
    private String avatarPath;

    /**
     * 用户所属地
     */
    private String region;

    public static UserInfoIntroVO objToVO(UserInfo userInfo) {
        if (userInfo == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserInfoIntroVO userInfoIntroVO = new UserInfoIntroVO();
        try {
            BeanUtils.copyProperties(userInfo, userInfoIntroVO);
        } catch (BusinessException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Bean复制属性错误");
        }
        return userInfoIntroVO;
    }

}

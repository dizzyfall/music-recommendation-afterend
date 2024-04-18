package com.dzy.constant;

/**
 * 文件常量
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/12  11:03
 */
public interface FileConstant {

    /**
     * 最大上传图片大小
     */
    Long IMAGE_MAXSIZE = 5 * 1024 * 1024L;

    String PROJECT_PATH = "C:\\Users\\DZY\\Desktop\\user";

    String DEFAULT_AVATAR_NAME = "default_avatar.png";

    String DEFAULT_BACKGROUND_NAME = "default_background.png";

    String DEFAULT_AVATAR_ABSOLUTE_PATH = PROJECT_PATH + "\\Default_User_Avatar" + "\\" + DEFAULT_AVATAR_NAME;

    String DEFAULT_BACKGROUND_ABSOLUTE_PATH = PROJECT_PATH + "\\Default_User_Background" + "\\" + DEFAULT_BACKGROUND_NAME;

}

package com.dzy.constant;

/**
 * 自定义响应状态类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/5  23:44
 */
public enum StatusCode {
    LOGIN_SUCESS("20001", "登录成功", ""),
    REGISTER_SUCCESS("20002", "注册成功", ""),
    STATE_SUCCESS("20003", "获取状态成功", ""),
    LOGOUT_SUCESS("20004", "退出登录成功", ""),
    CREATE_SUCESS("20005", "创建成功", ""),
    DELETE_SUCCESS("20006", "删除成功", ""),
    UPDATE_SUCESS("20007", "更新成功", ""),
    RETRIEVE_SUCCESS("20008", "查询成功", ""),


    DATAS_NULL_ERROR("40001", "请求数据为空", ""),
    PARAMS_ERROR("40002", "请求参数错误", ""),
    PARAMS_NULL_ERROR("40003", "请求参数为空", ""),

    NO_LOGIN_ERROR("40100", "未登录错误", ""),
    AUTHORITY_ERROR("40101", "无权限", ""),

    LOGIN_ERROR("40301", "登录失败", ""),
    REGISTER_ERROR("40302", "注册失败", ""),
    STATE_DELETE_ERROR("40303", "删除状态失败", ""),
    CREATE_ERROR("40305", "创建失败", ""),
    DELETE_ERROR("40306", "删除失败", ""),
    UPDATE_ERROR("40307", "更新失败", ""),
    RETRIEVE_ERROR("40308", "查询失败", ""),
    NOT_FOUND_ERROR("40400", "请求数据不存在", ""),

    SYSTEM_ERROR("50000", "服务器错误", ""),
    DATABASE_ERROR("50001", "数据库错误", "");

    /**
     * 状态码
     */
    private final String code;

    /**
     * 响应信息
     */
    private final String msg;

    /**
     * 响应详情
     */
    private final String description;

    StatusCode(String code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDesciption() {
        return description;
    }
}

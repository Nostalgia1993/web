package cn.enilu.flash.bean.enumeration;

import cn.enilu.flash.bean.exception.ServiceExceptionEnum;

/**
 * @author liunian-jk
 * @description TODO
 * @date 2024/8/16
 */
public enum BizExceptionEnum implements ServiceExceptionEnum {

    REQUEST_NULL(0, "请求参数为空"),
    REQUEST_RECORD_NOT_EXISTS(998, "请求记录不存在"),
    REQUEST_EMAIL_EXISTS(997, "邮箱地址已存在")
    ;

    private Integer code;
    private String message;

    BizExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public Integer getCode() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
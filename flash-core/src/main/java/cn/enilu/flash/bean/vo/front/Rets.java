package cn.enilu.flash.bean.vo.front;

import cn.enilu.flash.bean.exception.ServiceExceptionEnum;
import cn.enilu.flash.utils.Maps;

public class Rets {

    public static final Integer SUCCESS = 20000;
    public static final Integer FAILURE = 9999;
    public static final Integer TOKEN_EXPIRE = 50014;

    public static Ret success(Object data) {
        return new Ret(Rets.SUCCESS, "成功", data);
    }

    public static Ret failure(String msg) {
        return new Ret(Rets.FAILURE, msg, Maps.newHashMap());
    }

    public static Ret success() {
        return new Ret(Rets.SUCCESS, "成功", null);
    }

    public static Ret expire() {
        return new Ret(Rets.TOKEN_EXPIRE, "token 过期", null);
    }

    public static Ret failure(ServiceExceptionEnum serviceExceptionEnum) {
        return new Ret(serviceExceptionEnum.getCode(), serviceExceptionEnum.getMessage(), null);
    }

}

package com.tdeado.core.enums;

import com.tdeado.core.api.IErrorCode;

/**
 * REST API 错误码
 *
 * @author hubin
 * @since 2017-06-26
 */
public enum ApiErrorCode implements IErrorCode {
    /**
     * 失败
     */
    FAILED(-1, "操作失败"),
    /**
     * 无操作权限
     */
    NO_OPERATE_ACCESS(402, "无操作权限"),
    /**
     * 无访问权限
     */
    NO_ACCESS(403, "无访问权限"),
    /**
     * 成功
     */
    SUCCESS(200, "执行成功");

    private final long code;
    private final String msg;

    ApiErrorCode(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ApiErrorCode fromCode(long code) {
        ApiErrorCode[] ecs = ApiErrorCode.values();
        for (ApiErrorCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return String.format(" ErrorCode:{code=%s, msg=%s} ", code, msg);
    }
}

package com.tdeado.core.api;


/**
 * REST API 通用控制器
 *
 * @author hubin
 * @since 2018-06-08
 */
public class ApiController {



    /**
     * 请求成功
     *
     * @param data 数据内容
     * @param <T>  对象泛型
     * @return ignore
     */
    protected <T> R<T> success(T data) {
        return R.ok(data);
    }

    /**
     * 请求失败
     *
     * @param msg 提示内容
     * @return ignore
     */
    protected <T> R<T> failed(String msg) {
        return R.failed(msg);
    }

    /**
     * 请求失败
     *
     * @param errorCode 请求错误码
     * @return ignore
     */
    protected <T> R<T> failed(IErrorCode errorCode) {
        return R.failed(errorCode);
    }

}

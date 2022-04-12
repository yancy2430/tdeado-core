package com.tdeado.core.exception;

import com.tdeado.core.api.IErrorCode;

/**
 * REST API 请求异常类
 *
 * @author hubin
 * @since 2017-06-26
 */
public class ApiException extends RuntimeException {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5885155226898287919L;

	/**
     * 错误码
     */
    private IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}

package com.tdeado.core.global;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tdeado.core.api.IErrorCode;
import com.tdeado.core.api.R;
import com.tdeado.core.enums.ApiErrorCode;
import com.tdeado.core.exception.ApiException;
import com.tdeado.core.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 通用 Api Controller 全局异常处理
 * </p>
 *
 * @author jobob
 * @since 2018-09-27
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /***
     * 参数绑定异常
     * @date 2018/10/16
     * @param exception HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R<Object> messageNotReadable(HttpMessageNotReadableException exception){
        InvalidFormatException formatException = (InvalidFormatException)exception.getCause();
        List<JsonMappingException.Reference> e = formatException.getPath();
        String fieldName = "";
        for (JsonMappingException.Reference reference :e){
            fieldName = reference.getFieldName();
        }
        return R.failed(fieldName+"参数类型不匹配");
    }
    /***
     * 参数绑定异常
     * @date 2018/10/16
     * @param exception HttpMessageNotReadableException
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public R<Object> messageNotReadable(MissingServletRequestParameterException exception){

        return R.failed(exception.getParameterName()+"参数类型不匹配");
    }

    /**
     * <p>
     * 自定义 REST 业务异常
     * <p>
     *
     * @param e 异常类型
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R<Object> handleBadRequest(Exception e) {
        /*
         * 业务逻辑异常
         */
        if (e instanceof ApiException) {
            IErrorCode errorCode = ((ApiException) e).getErrorCode();
            if (null != errorCode) {
                return R.failed(errorCode);
            }
            return R.failed(e.getMessage());
        }

        /*
         * 参数校验异常
         */
        if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            if (null != bindingResult && bindingResult.hasErrors()) {
                List<Object> jsonList = new ArrayList<>();
                bindingResult.getFieldErrors().stream().forEach(fieldError -> {
                    Map<String, Object> jsonObject = new HashMap<>(2);
                    jsonObject.put("name", fieldError.getField());
                    jsonObject.put("msg", fieldError.getDefaultMessage());
                    jsonList.add(jsonObject);
                });
                return R.restResult(jsonList, ApiErrorCode.FAILED);
            }
        }
        e.printStackTrace();
        /**
         * 系统内部异常，打印异常栈
         */
        if (e instanceof RuntimeException){
            return R.failed(e.getMessage());
        }

        return R.failed(ApiErrorCode.FAILED);
    }
}
package com.tdeado.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 错误日志全打印
 */
public class LogUtil {
    public static String ExceptionToString(Exception e){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        return exception;
    }
}
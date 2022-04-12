package com.tdeado.core.controller;

import cn.hutool.core.util.StrUtil;
import com.tdeado.core.annotations.TdField;
import com.tdeado.core.api.ApiController;
import com.tdeado.core.api.R;
import com.tdeado.core.entity.Option;
import com.tdeado.core.enums.ApiErrorCode;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class BaseController extends ApiController {
    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;
    public <T> R<T> success(T obj){

        return R.ok(obj);
    }
    public <T> R<T> success(T obj,String msg){
        return R.restResult(obj, ApiErrorCode.SUCCESS.getCode(),msg);
    }
    public <T> R<T> failed(String message){

        return R.failed(message);
    }

    /**
     * 表头数据
     * @return
     */
    @RequestMapping("head")
    public R<List<Map<String,Object>>> head(){
        try {
            List<Map<String,Object>> head = new ArrayList<>();
            Class<?> cls = Class.forName("com.tdeado."+request.getRequestURI().split("/")[1]+".entity."+StrUtil.upperFirst(request.getRequestURI().split("/")[1])+ StrUtil.upperFirst(request.getRequestURI().split("/")[2]));
            for (Field declaredField : cls.getDeclaredFields()) {
                if (!declaredField.getName().equals("serialVersionUID")){
                    TdField tdField = declaredField.getAnnotation(TdField.class);
                    if (null!=tdField){
                        List<Option> options = Arrays.stream(tdField.options()).map(s -> {
                            String[] op = s.split(":");
                            return new Option().setLabel(op[0]).setValue(Integer.parseInt(op[1]));
                        }).collect(Collectors.toList());
                        Map<String,Object> headMap = new HashMap<>();
                        headMap.put("key",declaredField.getName());
                        headMap.put("title",tdField.title());
                        headMap.put("type",tdField.type());
                        headMap.put("hide",tdField.hide());
                        headMap.put("list",tdField.list());
                        headMap.put("width",tdField.width());
                        headMap.put("sort",tdField.sort());
                        if (options.size()>0) {
                            headMap.put("options",options);
                        }
                        head.add(headMap);
                    }
                }
            }
            return success(head);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return success(new ArrayList<>());
    }

    public static String TransactSQLInjection(String str)

    {

        return str.replaceAll(".*([';]+|(--)+).*", " ");

    }
}

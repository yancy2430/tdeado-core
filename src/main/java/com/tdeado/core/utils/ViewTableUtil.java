package com.tdeado.core.utils;

import com.tdeado.core.annotations.TdField;
import com.tdeado.core.api.R;
import com.tdeado.core.entity.Option;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 显示表工具
 */
public class ViewTableUtil {
    /**
     * 表头数据
     * @return
     */
    public static List<Map<String,Object>> createTableHeader(Class<?> cls){
        List<Map<String,Object>> head = new ArrayList<>();
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
        return head;
    }
}

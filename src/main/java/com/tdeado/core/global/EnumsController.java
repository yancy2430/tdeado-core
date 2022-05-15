package com.tdeado.core.global;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.EnumUtil;
import com.tdeado.core.api.R;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("enum")
public class EnumsController {
    Map<String, List<Object>> enums = new HashMap<>();
    public EnumsController() {
        for (Class<?> aClass : ClassUtil.scanPackageBySuper("com.tdeado", Enum.class)) {
            enums.put(aClass.getSimpleName(), EnumUtil.getFieldValues((Class<? extends Enum<?>>) aClass,"label"));
        }
    }
    @RequestMapping("{name}")
    public R<List<Object>> enumInfo(@PathVariable String name){
        return R.ok(enums.get(name));
    }
}
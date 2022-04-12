package com.tdeado.core.annotations;

import com.tdeado.core.entity.Option;
import com.tdeado.core.enums.InputType;
import org.springframework.beans.factory.support.BeanNameGenerator;

import java.lang.annotation.*;


/**
 * DTO字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface TdField {
    /**
     * 字段标题
     */
    String title();

    /**
     * 输入类型
     * @return
     */
    InputType type() default InputType.input;

    /**
     * 待选值 标签:值
     */
    String[] options() default {};
    /**
     * 待选值 标签:值
     */
    int width() default 0;

    /**
     * 是否编辑时隐藏
     */
    boolean hide() default false;
    /**
     * 是否排序
     */
    boolean sort() default false;
    /**
     * 是否列表展示
     */
    boolean list() default true;
}

package com.tdeado.core.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tdeado.core.jsonconversion.FieldBindSerializer;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@JacksonAnnotationsInside
@JsonSerialize(using = FieldBindSerializer.class)
public @interface FieldBind {
    Class<?> entity() default Void.class;
    String target();
    String alias() default "";
}

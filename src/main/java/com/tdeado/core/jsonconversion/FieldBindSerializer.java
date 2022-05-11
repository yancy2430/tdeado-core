package com.tdeado.core.jsonconversion;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.tdeado.core.annotations.FieldBind;

import java.io.IOException;

public class FieldBindSerializer extends JsonSerializer<Object> implements ContextualSerializer {
    private FieldBind field;
    private BeanProperty beanProperty;
    private FieldBindCacheService cacheService;


    public FieldBindSerializer() {
    }

    public FieldBindSerializer(FieldBind field, BeanProperty beanProperty) {
        this.field = field;
        this.beanProperty = beanProperty;
        this.cacheService = SpringUtil.getBean(FieldBindCacheService.class);
    }
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(value);
        if (null!=field){
            Object val = cacheService.getValue(field,value);
            gen.writeObjectField(field.alias(),val);
        }

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property!=null){
            FieldBind field = property.getAnnotation(FieldBind.class);
            if (field == null) {
                field = property.getContextAnnotation(FieldBind.class);
            }
            if (field != null) {
                return new FieldBindSerializer(field,property);
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(null);
    }
}

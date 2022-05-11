package com.tdeado.core.config;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
public class BaseConfigure {

    @Bean
    @ConditionalOnMissingBean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.ALWAYS);
        builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        builder.serializerByType(Enum.class, new JsonSerializer<Enum>() {
            @Override
            public void serialize(Enum anEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeObject(EnumUtil.getNameFieldMap((Class<? extends Enum<?>>) anEnum.getClass(),"label").get(anEnum.name()));
            }
        });
        builder.deserializerByType(Enum.class, new JsonDeserializer<Enum>() {
            @Override
            public Enum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
                Field res = ReflectUtil.getField(jsonParser.getParsingContext().getCurrentValue().getClass(), jsonParser.currentName());
                Class<Enum> clazz = (Class<Enum>)res.getType();
                Class<Enum<?>> clazze = (Class<Enum<?>>)res.getType();
                for (Map.Entry<String, Object> label : EnumUtil.getNameFieldMap(clazze, "label").entrySet()) {
                    if (label.getValue().toString().equals(jsonParser.getValueAsString())) {
                        return EnumUtil.fromString(clazz,label.getKey());
                    }
                }
                return null;
            }
        });
        final ObjectMapper objectMapper = builder.modules(
                new ParameterNamesModule(),
                new Jdk8Module(),
                new JavaTimeModule()
        ).featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
        SimpleModule simpleModule = new SimpleModule();
        // Integer 转为 String 防止 js 丢失精度
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        // 忽略 transient 关键词属性
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ObjectMapper mapper(@Qualifier("jackson2HttpMessageConverter") @Autowired MappingJackson2HttpMessageConverter messageConverter) {
        return messageConverter.getObjectMapper();
    }

}

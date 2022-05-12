package com.tdeado.core.request.factory;

import cn.hutool.core.util.EnumUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestFieldToEnumConvertFactory implements ConverterFactory<String, Enum> {

	private static final Map<Class<?>, Converter> CONVERTERS = new HashMap<>();

	@Override
	public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
		Converter<String, T> converter = CONVERTERS.get(targetType);
		if (converter == null) {
			converter = new RequestFieldToEnumConvert<>(targetType);
			CONVERTERS.put(targetType, converter);
		}
		return converter;
	}
	private class RequestFieldToEnumConvert<T extends Enum> implements Converter<String, T> {
		private Map<String, T> enumMap = new HashMap<>();

		public  RequestFieldToEnumConvert() {}

		public RequestFieldToEnumConvert(Class<T> enumType) {
			for (Map.Entry<String, Object> label : EnumUtil.getNameFieldMap((Class<? extends Enum<?>>) enumType, "label").entrySet()) {
				Enum e = EnumUtil.fromString(enumType, label.getKey());
				enumMap.put(label.getValue().toString(), (T) e);
			}
		}

		@Override
		public T convert(String source) {
			T t = enumMap.get(source);
			if (t == null) {
				throw new RuntimeException("枚举类型不存在");
			}
			return t;
		}
	}
}
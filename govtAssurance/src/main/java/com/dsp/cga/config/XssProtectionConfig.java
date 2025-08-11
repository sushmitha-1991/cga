package com.dsp.cga.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class XssProtectionConfig {

	@Bean
	public Module xssEscapeModule() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(String.class, new JsonSerializer<>() {
			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeString(StringEscapeUtils.escapeHtml4(value));
			}
		});
		return module;
	}

	@Bean
	public FilterRegistrationBean<OncePerRequestFilter> xssFilter() {
		FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				HttpServletRequest sanitizedRequest = new HttpServletRequestWrapper(request) {
					@Override
					public String getParameter(String name) {
						return sanitize(super.getParameter(name));
					}

					@Override
					public String[] getParameterValues(String name) {
						String[] values = super.getParameterValues(name);
						if (values == null)
							return null;
						return Arrays.stream(values).map(this::sanitize).toArray(String[]::new);
					}

					@Override
					public Map<String, String[]> getParameterMap() {
						Map<String, String[]> paramMap = super.getParameterMap();
						Map<String, String[]> sanitizedMap = new HashMap<>();
						paramMap.forEach((key, values) -> {
							sanitizedMap.put(key, Arrays.stream(values).map(this::sanitize).toArray(String[]::new));
						});
						return sanitizedMap;
					}

					private String sanitize(String input) {
						return input != null ? Jsoup.clean(input, Safelist.basic()) : null;
					}
				};
				filterChain.doFilter(sanitizedRequest, response);
			}
		});
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
		return registrationBean;
	}

	@ControllerAdvice
	public static class XssRequestBodyAdvice extends RequestBodyAdviceAdapter {

		@Override
		public boolean supports(MethodParameter methodParameter, Type targetType,
				Class<? extends HttpMessageConverter<?>> converterType) {
			return true;
		}

		@Override
		public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
				Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
			sanitizeObject(body, new IdentityHashMap<>());
			return body;
		}

		private void sanitizeObject(Object object, Map<Object, Boolean> visited) {
			if (object == null || visited.containsKey(object))
				return;

			visited.put(object, true);

			if (object instanceof String s) {
				// Strings are sanitized at field level
				return;
			} else if (object instanceof Iterable<?> iterable) {
				for (Object item : iterable) {
					sanitizeObject(item, visited);
				}
			} else if (object instanceof Map<?, ?> map) {
				for (Map.Entry<?, ?> entry : map.entrySet()) {
					sanitizeObject(entry.getValue(), visited);
				}
			} else if (isPrimitiveOrWrapper(object.getClass()) || object.getClass().isEnum()
					|| object.getClass().getPackageName().startsWith("java.")) {
				return;
			} else {
				for (Field field : object.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					try {
						Object value = field.get(object);
						if (value instanceof String str) {
							field.set(object, Jsoup.clean(str, Safelist.basic()));
						} else {
							sanitizeObject(value, visited);
						}
					} catch (IllegalAccessException ignored) {
					}
				}
			}
		}

		private boolean isPrimitiveOrWrapper(Class<?> clazz) {
			return clazz.isPrimitive() || clazz == Boolean.class || clazz == Byte.class || clazz == Character.class
					|| clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == Float.class
					|| clazz == Double.class || clazz == Void.class;
		}
	}
}


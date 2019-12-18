package com.xyjsoft.core.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 跨域配置
 * @author gsh456
 * @date 2019-03-15 11:10
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Bean("jackson2ObjectMapperBuilderCustomizer")
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
	    Jackson2ObjectMapperBuilderCustomizer customizer = new Jackson2ObjectMapperBuilderCustomizer() {
	        @Override
	        public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
	            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance)
	                    .serializerByType(Long.TYPE, ToStringSerializer.instance);
	        }
	    };
	    return customizer;
	}
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")	// 允许跨域访问的路径
        .allowedOrigins("*")	// 允许跨域访问的源
        .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")	// 允许请求方法
        .maxAge(168000)	// 预检间隔时间
        .allowedHeaders("*")  // 允许头部设置
        .allowCredentials(true);	// 是否发送cookie
    }
}
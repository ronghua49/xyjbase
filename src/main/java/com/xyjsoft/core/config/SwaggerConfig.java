package com.xyjsoft.core.config;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 * @author Louis
 * @date Oct 29, 2018
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@EnableConfigurationProperties({SwaggerProperties.class})
public class SwaggerConfig{
	
    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi(){
    	// 添加请求参数，我们这里把token作为请求头部参数传入后端
		ParameterBuilder parameterBuilder = new ParameterBuilder();  
		List<Parameter> parameters = new ArrayList<Parameter>();  
		parameterBuilder.name("token").description("令牌")
			.modelRef(new ModelRef("string")).parameterType("header").required(false).build();  
		parameters.add(parameterBuilder.build());  
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build().globalOperationParameters(parameters);
    }
    @Autowired
	SwaggerProperties properties;
    private ApiInfo apiInfo(){
    	/*return new ApiInfoBuilder()
    			.title("XYJSOFT API Doc")
    			.description("This is a restful api document of 新永丰.")
    			.version("1.0")
    			.build();*/
    	ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
		apiInfoBuilder.description(properties.getDescription());
		apiInfoBuilder.title(properties.getTitle());
		apiInfoBuilder.version(properties.getVersion());
		if(StringUtils.isNotEmpty(properties.getLicense())){
			apiInfoBuilder.license(properties.getLicense());
		}
		if(StringUtils.isNotEmpty(properties.getLicenseUrl())){
			apiInfoBuilder.licenseUrl(properties.getLicenseUrl());
		}
		Contact contact = properties.getContact();
		if(contact!=null){
			apiInfoBuilder.contact(new Contact(contact.getName(),contact.getUrl(),contact.getEmail()));
		}
		return apiInfoBuilder.build();
    }
    
}
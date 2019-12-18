package com.xyjsoft.core.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	@Autowired
	XyjconfigProperties xyjconfigProperties;
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		//60s
        requestFactory.setConnectTimeout(xyjconfigProperties.getRestTemplateTimeOut());
        requestFactory.setReadTimeout(50*1000);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
	    List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
	    for (HttpMessageConverter<?> httpMessageConverter : list) {
	        if(httpMessageConverter instanceof StringHttpMessageConverter) {
	            ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
	        }
	    }
	    return restTemplate;
	}
}

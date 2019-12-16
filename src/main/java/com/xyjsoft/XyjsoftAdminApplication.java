package com.xyjsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动器
 * @author Louis
 * @date Oct 29, 2018
 */
@EnableFeignClients
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication(scanBasePackages={"com.xyjsoft"})
public class XyjsoftAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(XyjsoftAdminApplication.class, args);
	}

}

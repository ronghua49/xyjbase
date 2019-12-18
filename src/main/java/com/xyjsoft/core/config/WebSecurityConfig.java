package com.xyjsoft.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.xyjsoft.core.security.JwtAuthenticationFilter;
import com.xyjsoft.core.security.JwtAuthenticationProvider;

/**
 * Spring Security Config
 *
 * @author gsh456
 * @date 2019-03-15 11:10
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService UserDetailsServiceImpl;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件
        auth.authenticationProvider(new JwtAuthenticationProvider(UserDetailsServiceImpl));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 csrf, 由于使用的是JWT，我们这里不需要csrf
        http.cors().and().csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                // 跨域预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // web jars
                .antMatchers("/webjars/**").permitAll()
                // 查看SQL监控（druid）
                .antMatchers("/druid/**").permitAll()
                // 首页和登录页面
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/SubsidiaryLogin").permitAll()
                //小程序登录
                .antMatchers("/appUser/miniAppLogin/**").permitAll()
                //网站应用登录
                .antMatchers("/appUser/webWxLogin/**").permitAll()
                // swagger
                .antMatchers("/doc.html").permitAll()
                .antMatchers("/hystrix.stream").permitAll()
                .antMatchers("/docs.html").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs**").permitAll()
                // 验证码
                .antMatchers("/captcha.jpg**").permitAll()
                //小程序服务器验证
                .antMatchers("/wxportal/core/**").permitAll()
                //通用导出
                .antMatchers("/export/**").permitAll()
                //通用打印
                .antMatchers("/print/**").permitAll()
                //微信支付回调
                .antMatchers("/XyjPay/payWxResult").permitAll()
                //建行支付回调
                .antMatchers("/XyjPay/payccbResult").permitAll()
                // 服务监控
                .antMatchers("/actuator/**").permitAll()
                //接受前端数据到redis信息 目前东风日产用
                .antMatchers("/sysRedis/saveloginInfo").permitAll()
                //工作流编辑器
                .antMatchers("/modeler.html").permitAll()
                //工作流编辑器
                .antMatchers("/editor-app/**").permitAll()
                //工作流编辑器接口放行
                .antMatchers("/model/**").permitAll()
                //设备心跳
                .antMatchers("/deviceOnlineStatus/deviceHeart/**").permitAll()
                // 静态资源
                .antMatchers("/static/**").permitAll()
                // 其他所有请求需要身份认证
                .anyRequest().authenticated();
        // 退出登录处理器
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        // token验证过滤器
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
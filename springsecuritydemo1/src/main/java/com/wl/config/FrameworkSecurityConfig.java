package com.wl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/8/27
 */
@Configuration
@EnableWebSecurity
public class FrameworkSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationHandlerConfig authenticationHandlerConfig;

    @Resource
    private AccessDeniedHandlerConfig accessDeniedHandlerConfig;


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("{noop}123456").roles("USER");
//    }

    /**
     * HttpSecurity构建 DefaultSecurityFilterChain
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //表单
//        http.authorizeRequests().anyRequest().authenticated().and().formLogin();
//        http.sessionManagement().sessionFixation().migrateSession().maximumSessions(1).maxSessionsPreventsLogin(true);

        //自定义认证方式

        //csrf设置 使用双重cookie校验
        CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
        cookieCsrfTokenRepository.setCookiePath("/");
        cookieCsrfTokenRepository.setCookieHttpOnly(false);
        http.csrf().ignoringAntMatchers("/login").csrfTokenRepository(cookieCsrfTokenRepository);

        //停用form表单登录
        http.formLogin().disable();

        //防御点击劫持 如果有html页面
        http.headers().frameOptions().disable();

        //自定义认证
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        //url授权设置
        http.authorizeHttpRequests().antMatchers("/login").permitAll().anyRequest().authenticated();

        // 认证 授权失败设置
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandlerConfig).authenticationEntryPoint(authenticationHandlerConfig);
    }

    @Bean
    public SelfSessionFilter authenticationFilter() throws Exception {
        SelfSessionFilter selfSessionFilter = new SelfSessionFilter();
        selfSessionFilter.setAuthenticationManager(authenticationManagerBean());
        selfSessionFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        selfSessionFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        selfSessionFilter.setSessionAuthenticationStrategy(sessionStrategy());
        return selfSessionFilter;
    }

    public SessionAuthenticationStrategy sessionStrategy() {

        SessionRegistryImpl sessionRegistry = new SessionRegistryImpl();

        // 并发会话数
        ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
        concurrentSessionControlAuthenticationStrategy.setMaximumSessions(2);
        concurrentSessionControlAuthenticationStrategy.setExceptionIfMaximumExceeded(true);

        //session 会话固定防护策略
        SessionFixationProtectionStrategy sessionFixationProtectionStrategy = new SessionFixationProtectionStrategy();


        //会话注册
        RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy = new RegisterSessionAuthenticationStrategy(sessionRegistry);

        List<SessionAuthenticationStrategy> objects = new ArrayList<>();

        objects.add(concurrentSessionControlAuthenticationStrategy);
        objects.add(sessionFixationProtectionStrategy);
        objects.add(registerSessionAuthenticationStrategy);

        return new CompositeSessionAuthenticationStrategy(objects);
    }
}
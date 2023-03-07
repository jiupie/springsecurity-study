package com.wl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.*;

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


    /**
     * HttpSecurity构建 DefaultSecurityFilterChain
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //表单
        http.authorizeRequests().anyRequest().authenticated().and().formLogin();
        http.sessionManagement().sessionFixation().migrateSession().maximumSessions(1).maxSessionsPreventsLogin(true);

        //自定义
        //session 会话固定策略
        // 最大会话数
//
//        http.csrf().disable();
//        http.formLogin().disable();
//
//        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.sessionManagement().maximumSessions(1);
//
//        http.authorizeHttpRequests()
//                .antMatchers("/login").permitAll()
//                .anyRequest().authenticated();
//
//        http.exceptionHandling()
//                .accessDeniedHandler(accessDeniedHandlerConfig)
//                .authenticationEntryPoint(authenticationHandlerConfig);
    }

//    @Bean
    public SelfSessionFilter authenticationFilter() throws Exception {
        SelfSessionFilter selfSessionFilter = new SelfSessionFilter();
        selfSessionFilter.setAuthenticationManager(authenticationManagerBean());
        selfSessionFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        selfSessionFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        selfSessionFilter.setSessionAuthenticationStrategy(sessionStrategy());
        return selfSessionFilter;
    }

//    @Bean
    public SessionAuthenticationStrategy sessionStrategy() {
        // 使用默认的会话固定保护策略，也可以自定义其他策略
        SessionFixationProtectionStrategy sessionFixationProtectionStrategy = new SessionFixationProtectionStrategy();

        SessionRegistryImpl sessionRegistry = new SessionRegistryImpl();
        ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
        concurrentSessionControlAuthenticationStrategy.setMaximumSessions(1);

        RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy = new RegisterSessionAuthenticationStrategy(sessionRegistry);


        List<SessionAuthenticationStrategy> objects = new ArrayList<>();
        objects.add(sessionFixationProtectionStrategy);
        objects.add(registerSessionAuthenticationStrategy);
        objects.add(concurrentSessionControlAuthenticationStrategy);

        return new CompositeSessionAuthenticationStrategy(objects);
    }
}
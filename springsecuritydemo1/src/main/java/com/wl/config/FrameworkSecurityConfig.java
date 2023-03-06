package com.wl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/8/27
 */
@Configuration
//public class FrameworkSecurityConfig<S extends Session> extends WebSecurityConfigurerAdapter {
public class FrameworkSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationHandlerConfig authenticationHandlerConfig;

    @Resource
    private AccessDeniedHandlerConfig accessDeniedHandlerConfig;

    /**
     * websecurity 是构建filter
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
    }


    /**
     * HttpSecurity构建 DefaultSecurityFilterChain
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin().disable();
//        http.formLogin().loginPage("/login");
        //session 会话固定策略
        // 最大会话数

        http.sessionManagement().sessionFixation().migrateSession();
        http
//                //授权
                .authorizeHttpRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated();

//                .hasAnyRole("admin")


        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandlerConfig)
                .authenticationEntryPoint(authenticationHandlerConfig);
    }


//    @Resource
//    private FindByIndexNameSessionRepository<S> sessionRepository;
//
//    @Bean
//    public SpringSessionBackedSessionRegistry<S> sessionRegistry() {
//        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
//    }
}
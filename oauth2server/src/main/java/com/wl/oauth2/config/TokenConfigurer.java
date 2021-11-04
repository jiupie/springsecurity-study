package com.wl.oauth2.config;


import com.wl.oauth2.config.bean.SecurityProperties;
import com.wl.oauth2.config.filter.TokenFilter;
import com.wl.oauth2.utils.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 配置过滤器
 */
@AllArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
     private final TokenProvider tokenProvider;

     private final SecurityProperties properties;

    @Override
    public void configure(HttpSecurity httpSecurity)  {
        TokenFilter customFilter = new TokenFilter(tokenProvider, properties);

        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

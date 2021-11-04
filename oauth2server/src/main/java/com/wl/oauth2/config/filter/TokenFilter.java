package com.wl.oauth2.config.filter;


import com.wl.oauth2.config.bean.SecurityProperties;
import com.wl.oauth2.utils.TokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@AllArgsConstructor
@Slf4j
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws  ServletException, IOException {

        String token = resolveToken((HttpServletRequest) request);
        if (StringUtils.hasText(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    /**
     *
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(properties.getTokenStartWith(), "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}

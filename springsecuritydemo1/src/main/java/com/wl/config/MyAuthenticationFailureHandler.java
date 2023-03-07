package com.wl.config;

import com.wl.utils.JsonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "登录失败:" + exception.getMessage());
        map.put("code", -1);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JsonUtil.toStr(map));
    }
}

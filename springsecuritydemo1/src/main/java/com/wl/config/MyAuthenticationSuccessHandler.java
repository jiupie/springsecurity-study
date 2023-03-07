package com.wl.config;

import com.wl.utils.JsonUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {


        Map<String, Object> map = new HashMap<>();
        map.put("msg", "登录成功");
        map.put("code", 200);
        map.put("authentication", authentication);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JsonUtil.toStr(map));
    }
}

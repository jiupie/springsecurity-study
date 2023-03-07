package com.wl.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/8/27
 */
@RestController
@Slf4j
public class AuthenticationController {
    @Resource
    private AuthenticationManagerBuilder authenticationManagerBuilder;

//    @PostMapping("/login")
//    public String login(@RequestBody LoginReq loginReq) {
//        String username = loginReq.getUsername();
//        String pwd = loginReq.getPwd();
//        log.info("username:{},pwd:{}", username, pwd);
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, pwd);
//        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
//        if (authenticate != null) {
//            SecurityContextHolder.getContext().setAuthentication(authenticate);
//        }
//        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true);
//        session.setAttribute("userId", ((UserInfoDTO) authenticate.getPrincipal()).getUsername());
//        return "success";
//    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyAuthority('admin')")
    public String hello() {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();

        return "hello,my name is " + SecurityContextHolder.getContext().getAuthentication().getName() + ";session=" + session.getId();
    }


    @GetMapping("/h")
    public String h() {
        return "h";
    }
}

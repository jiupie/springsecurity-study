package com.wl.demo1.web;

import com.wl.demo1.daomain.User;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {
    //    @Secured({"ROLE_admin"})
//    @PreAuthorize("hasRole('ROLE_admin')")
    @GetMapping("/hello")
    @Secured({"admin"})
    public User s(String name){
        User user = new User();
        user.setName(name);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes requestAttributes1 = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request1 = requestAttributes.getRequest();
        HttpServletRequest request = requestAttributes1.getRequest();
        return user;
    }
    @GetMapping("/test")
//    @PreAuthorize("hasRole('ROLE_admin')")
//    @PreAuthorize("hasAuthority('test:list')")
//    @Secured({"admin"})
//    @Secured()
    public String test(String type){
         return type;
    }
}

package com.wl.demo2.service.impl;

import com.wl.demo2.service.RoleService;
import com.wl.demo2.service.UserService;
import com.wl.demo2.service.dto.JwtUserDto;
import com.wl.demo2.service.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证
 */
@Service("userDetailsService")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    public static ConcurrentHashMap<String, JwtUserDto> userInfo = new ConcurrentHashMap<>();


    private final UserService userService;

    private final RoleService roleService;

    @Override
    public JwtUserDto loadUserByUsername(String userName) throws UsernameNotFoundException {
        JwtUserDto cacheJwtUserDto = userInfo.get(userName);
        if (cacheJwtUserDto != null) {
            return cacheJwtUserDto;
        }
        //从数据库中获取
        UserDto userDto = userService.findUsrByUserName(userName);
        List<GrantedAuthority> grantedAuthorities = roleService.mapToGrantedAuthorities(userDto);
        JwtUserDto jwtUserDto = new JwtUserDto(userDto, grantedAuthorities);
        userInfo.put(userName,jwtUserDto);
        return jwtUserDto;
    }
}

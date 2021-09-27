package com.wl.demo2.service;

import com.wl.demo2.service.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface RoleService {
    List<GrantedAuthority> mapToGrantedAuthorities(UserDto user);

}

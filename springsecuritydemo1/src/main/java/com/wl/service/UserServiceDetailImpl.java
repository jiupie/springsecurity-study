package com.wl.service;

import com.wl.model.dto.UserInfoDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * UsernamePasswordAuthenticationToken的服务类
 *
 * @author dile
 */
@Service
public class UserServiceDetailImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("root".equals(username) || "admin".equals(username)) {
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setUsername(username);
            userInfoDTO.setPassword("{noop}123456");
            ArrayList<String> roles = new ArrayList<>();
            roles.add("admin");
            userInfoDTO.setRoles(roles);
            return userInfoDTO;
        }
        return null;
    }
}

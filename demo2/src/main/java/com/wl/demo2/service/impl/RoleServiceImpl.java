package com.wl.demo2.service.impl;

import com.wl.demo2.dao.MenuDao;
import com.wl.demo2.dao.RoleDao;
import com.wl.demo2.daomain.Menu;
import com.wl.demo2.service.RoleService;
import com.wl.demo2.service.dto.RoleSmallDto;
import com.wl.demo2.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;

    @Override
    public List<GrantedAuthority> mapToGrantedAuthorities(UserDto user) {

        List<RoleSmallDto> roleSmallDtos = roleDao.findRolesByUserId(user.getId());
        List<Long> roleIds = new ArrayList<>();
        roleSmallDtos.forEach(roleSmallDto -> roleIds.add(roleSmallDto.getId()));
        List<Menu> menus = menuDao.findAllByRole(roleIds);
//        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        for (Menu menu : menus) {
//            if(Objects.isNull(menu.getPermission())){
//                continue;
//            }
//            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(menu.getPermission());
//            grantedAuthorities.add(simpleGrantedAuthority);
//        }


        Set<String> set = menus.stream().filter(menu -> StringUtils.hasText(menu.getPermission())).map(Menu::getPermission).collect(Collectors.toSet());

        return set.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}

package com.wl.demo2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wl.demo2.daomain.Role;
import com.wl.demo2.service.dto.MenuDto;
import com.wl.demo2.service.dto.RoleSmallDto;
import com.wl.demo2.service.dto.UserDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends BaseMapper<Role> {

    List<RoleSmallDto> findRolesByUserId(Long userId);

}

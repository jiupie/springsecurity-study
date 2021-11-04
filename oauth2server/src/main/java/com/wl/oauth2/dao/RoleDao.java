package com.wl.oauth2.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wl.oauth2.daomain.Role;
import com.wl.oauth2.service.dto.RoleSmallDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends BaseMapper<Role> {

    List<RoleSmallDto> findRolesByUserId(Long userId);

}

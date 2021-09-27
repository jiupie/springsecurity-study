package com.wl.demo2.dao;

import com.wl.demo2.service.dto.RoleSmallDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleDaoTest {
    @Autowired
    private RoleDao roleDao;
    @Test
    public void test(){
        List<RoleSmallDto> rolesByUserId = roleDao.findRolesByUserId(1L);
        System.out.println(rolesByUserId);
    }

}
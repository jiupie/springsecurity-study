package com.wl.demo2.dao;

import com.wl.demo2.daomain.User;
import com.wl.demo2.service.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void test() {
        UserDto admin = userDao.findUserByUserName("admin");
        System.out.println(admin);

    }

}
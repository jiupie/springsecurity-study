package com.wl.demo2.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuDaoTest {
    @Autowired
    private MenuDao menuDao;

    @Test
    void findAllByRole() {
        System.out.println(menuDao.findAllByRole(Arrays.asList(1L)));
    }
}
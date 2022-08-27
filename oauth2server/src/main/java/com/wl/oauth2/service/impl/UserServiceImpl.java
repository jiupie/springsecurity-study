package com.wl.oauth2.service.impl;


import com.wl.oauth2.dao.UserDao;
import com.wl.oauth2.service.UserService;
import com.wl.oauth2.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDto findUsrByUserName(String userName) {

        return userDao.findUserByUserName(userName);
    }
}

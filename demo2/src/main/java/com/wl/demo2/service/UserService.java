package com.wl.demo2.service;

import com.wl.demo2.service.dto.UserDto;


public interface UserService {
    UserDto findUsrByUserName(String userName);
}

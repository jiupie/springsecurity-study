package com.wl.demo2.dao;

import com.wl.demo2.service.dto.UserDto;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao   {
    UserDto findUserByUserName(String userName);
}

package com.wl.oauth2.dao;


import com.wl.oauth2.service.dto.UserDto;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao   {
    UserDto findUserByUserName(String userName);
}

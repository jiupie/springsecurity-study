package com.wl.oauth2.service;


import com.wl.oauth2.service.dto.UserDto;

public interface UserService {
   UserDto findUsrByUserName(String userName);
}

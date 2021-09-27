package com.wl.demo2.service;

import com.wl.demo2.daomain.User;
import com.wl.demo2.service.dto.UserDto;
import org.springframework.stereotype.Service;


public interface UserService {
   UserDto findUsrByUserName(String userName);
}

package com.wl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PwdEncodeTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void test(){
        String pwd = passwordEncoder.encode("sdafa");
        System.out.println(pwd);

    }
}

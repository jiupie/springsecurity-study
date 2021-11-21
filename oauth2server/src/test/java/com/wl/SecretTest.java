package com.wl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class SecretTest {

    @Test
    public void test(){
        System.out.println(new BCryptPasswordEncoder().encode("mate"));

    }
}

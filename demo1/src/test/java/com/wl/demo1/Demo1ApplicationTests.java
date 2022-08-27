package com.wl.demo1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class Demo1ApplicationTests {

    @Test
    void contextLoads() {
        ArrayList<Long> list = new ArrayList<>();

        list.add(new Long(2222L));
        list.add(new Long(5555L));
        list.add(new Long(4444L));
        Long s=4444L;
        System.out.println(list.contains(s));
    }

}

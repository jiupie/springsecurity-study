package com.wl.demo2.dao;

import com.wl.demo2.daomain.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDao {
    List<Menu> findAllByRole(List<Long> roleIds);
}

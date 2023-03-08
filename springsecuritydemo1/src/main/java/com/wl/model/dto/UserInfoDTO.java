package com.wl.model.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserInfoDTO implements UserDetails {

    private String username;
    private String password;
    private List<GrantedAuthority> roles = new ArrayList<>();

    public List<String> getRolesStr() {
        return this.roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public void setRoles(List<String> roleList) {
        if (!CollectionUtils.isEmpty(roleList)) {
            roleList.forEach(role -> roles.add(new SimpleGrantedAuthority(role)));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * 用户密码
     *
     * @return /
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 用户名称
     *
     * @return /
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 用户账户是否没有过期
     *
     * @return /
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用户是否 没有被锁定
     *
     * @return /
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 认证是否 没有过期，过期时需要重新登录
     *
     * @return /
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 该用户是否能够使用
     *
     * @return /
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

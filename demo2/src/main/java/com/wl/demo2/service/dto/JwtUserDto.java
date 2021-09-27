package com.wl.demo2.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@AllArgsConstructor
@Getter
public class JwtUserDto implements UserDetails {
    private final UserDto userDto;

    @JsonIgnore
    private final List<GrantedAuthority> authorities;

    @Override
    @JsonIgnore
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return userDto.getUsername();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return userDto.getEnabled();
    }
}

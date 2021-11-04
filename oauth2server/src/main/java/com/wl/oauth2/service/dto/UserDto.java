package com.wl.oauth2.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@ToString
public class UserDto {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Set<RoleSmallDto> roles;

    private String username;

    private String nickName;

    private String gender;

    private String phone;

    private String email;

    private String avatarName;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private Boolean isAdmin;

    private Boolean enabled;

    private Date pwdResetTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto dto = (UserDto) o;
        return Objects.equals(id, dto.id) &&
                Objects.equals(username, dto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}

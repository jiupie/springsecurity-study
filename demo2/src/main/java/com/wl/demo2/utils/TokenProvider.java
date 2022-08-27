package com.wl.demo2.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wl.demo2.config.bean.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "user";
    @Autowired
    private SecurityProperties securityProperties;

    private JwtBuilder jwtBuilder;
    private JwtParser jwtParser;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] base64Secret = Decoders.BASE64.decode(securityProperties.getBase64Secret());
        SecretKey secretKey = Keys.hmacShaKeyFor(base64Secret);
        jwtBuilder = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS512);
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public String createToken(Authentication authentication) {
        //获取权限
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(";"));
        return jwtBuilder.setId(IdUtil.simpleUUID())
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .compact();
    }

    /**
     * 获取鉴权信息
     *
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = this.getClaims(token);
        Object authoritiesStr = claims.get(AUTHORITIES_KEY);
        Collection<? extends GrantedAuthority> authorities =
                ObjectUtil.isNotEmpty(authoritiesStr) ?
                        Arrays.stream(authoritiesStr.toString().split(","))
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()) : Collections.emptyList();

        User user = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    public Claims getClaims(String token) {
        return jwtParser
                .parseClaimsJws(token).getBody();
    }
}

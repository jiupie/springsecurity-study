package com.wl.oauth2.config;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class RedisAuthenticationCodeService extends RandomValueAuthorizationCodeServices {

    private static final String AUTH_CODE_KEY = "auth_code";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected OAuth2Authentication remove(String code) {
        try {
            byte[] bytes = (byte[]) redisTemplate.opsForHash().get(AUTH_CODE_KEY.getBytes(StandardCharsets.UTF_8), code.getBytes(StandardCharsets.UTF_8));
            if (bytes == null || ArrayUtil.isEmpty(bytes)) {
                return null;
            }
            OAuth2Authentication authentication = SerializeUtil.deserialize(bytes);
            if (null != authentication) {
                redisTemplate.opsForHash().delete(AUTH_CODE_KEY.getBytes(StandardCharsets.UTF_8), code.getBytes(StandardCharsets.UTF_8));
            }
            return authentication;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        try {
            redisTemplate.opsForHash().put(AUTH_CODE_KEY.getBytes(StandardCharsets.UTF_8), code.getBytes(StandardCharsets.UTF_8),
                    SerializeUtil.serialize(authentication));
            log.info("保存authentication code: {}至redis", code);
        } catch (Exception e) {
            log.error("保存authentication code至redis失败", e);
        }
    }


}
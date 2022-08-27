package com.wl.oauth2.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.SerializeUtil;
import com.wl.oauth2.common.Oauth2Constant;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 自定义客户端信息加载，把这些信息加载入redis中
 */
@Slf4j
@Setter
@Service
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisTemplate redisTemplate;

    public ClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 从redis里读取ClientDetails
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        //如果redis中没有就从数据库中全部加载进入redis中
        byte[] bytes = (byte[]) redisTemplate.opsForValue().get(clientKey(clientId).getBytes(StandardCharsets.UTF_8));
        ClientDetails clientDetails = null;
        if (bytes == null || ArrayUtil.isEmpty(bytes)) {
            clientDetails = getCacheClient(clientId);
        } else {
            clientDetails = SerializeUtil.deserialize(bytes);
        }
        return clientDetails;
    }

    /**
     * 调用父类从数据库中获取客服端具体信息，然后加入redis中
     *
     * @param clientId 客服端ID
     * @return /
     */
    private ClientDetails getCacheClient(String clientId) {
        ClientDetails clientDetails = super.loadClientByClientId(clientId);
        try {
            if (Objects.nonNull(clientDetails)) {
                redisTemplate.opsForValue().set(clientKey(clientId).getBytes(StandardCharsets.UTF_8),
                        SerializeUtil.serialize(clientDetails), Oauth2Constant.CLIENT_TIME);
                log.debug("Cache clientId:{}, clientDetails:{}", clientId, clientDetails);
            }
        } catch (Exception e) {
            log.error("Exception for clientId:{}, message:{}", clientId, e.getMessage());
        }
        return clientDetails;
    }

    private String clientKey(String clientId) {
        return Oauth2Constant.CLIENT_TABLE + ":" + clientId;
    }


}

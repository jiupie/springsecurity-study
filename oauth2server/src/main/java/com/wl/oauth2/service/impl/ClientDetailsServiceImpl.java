package com.wl.oauth2.service.impl;

import com.wl.oauth2.utils.RedisUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;

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
    private RedisUtils redisUtils;

    public ClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 从redis里读取ClientDetails
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        //TODO: 如果redis中没有就从数据库中全部加载进入redis中

        return null;
    }


}

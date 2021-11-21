package com.wl.oauth2.config;

import com.wl.oauth2.common.Oauth2Constant;
import com.wl.oauth2.config.bean.SecurityProperties;
import com.wl.oauth2.service.dto.JwtUserDto;
import com.wl.oauth2.service.impl.ClientDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
//使能oauth2 server
@EnableAuthorizationServer
public class AuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisAuthenticationCodeService redisAuthenticationCodeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 客户端服务类
     */
    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;

    /**
     * 配置client 客户端信息
     *
     * @param clients /
     * @throws Exception /
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                //配置clientId
//                .withClient("test")
//                //认证类型
//                .authorizedGrantTypes("refresh_token", "authorization_code")
//                //token有效时间
//                .accessTokenValiditySeconds(60 * 60)
//                //refresh token有效时间
//                .refreshTokenValiditySeconds(60 * 60 * 2)
//                //授权范围
//                .scopes("all")
//                //回调地址
//                .redirectUris("http://www.baidu.com")
////                .resourceIds("")
//                //秘钥
//                .secret(passwordEncoder.encode("sdfadsxcxzcdsfasdfd"))
//                //是否自动授权
//                .autoApprove(true);
        clientDetailsService.setSelectClientDetailsSql(Oauth2Constant.SELECT_CLIENT_DETAIL_SQL);
        clientDetailsService.setDeleteClientDetailsSql(Oauth2Constant.FIND_CLIENT_DETAIL_SQL);
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 端点认证 配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //设置token 存储
                .tokenStore(tokenStore())
                //userDetails 用户名密码登录的时候数据库中校验是否正确
                .userDetailsService(userDetailsService)
                //授权码存储方式
                .authorizationCodeServices(redisAuthenticationCodeService)

                //认证管理
                .authenticationManager(authenticationManager);

        endpoints
                //设置tokenservice
                .tokenServices(defaultTokenServices())
                .accessTokenConverter(jwtAccessTokenConverter());

    }


    /**
     * jwt redis存储token
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
//        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    private DefaultTokenServices defaultTokenServices() {
        //可以自定义实现，实现只允许只有用户只能在一个地方登录等等
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());

        //支持刷新token
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);

        //设置客户端服务处理
        defaultTokenServices.setClientDetailsService(clientDetailsService);

        //设置token增强器
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);


        return defaultTokenServices;
    }

    /**
     * 增强token ，把用户信息存入token中
     *
     * @return /
     */
    public TokenEnhancer tokenEnhancer() {
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//                if (authentication.isClientOnly()) {
                if (authentication.getUserAuthentication() == null) {
                    return accessToken;
                }
                Map<String, Object> additionalInfo = new HashMap<>(2);
                JwtUserDto jwtUserDto = (JwtUserDto) authentication.getUserAuthentication().getPrincipal();
                if (Objects.nonNull(jwtUserDto)) {
                    additionalInfo.put(Oauth2Constant.USER_ID, jwtUserDto.getUserDto().getId());
                    additionalInfo.put(Oauth2Constant.USER_NAME, jwtUserDto.getUserDto().getUsername());
                }
                //accessToken的附加信息
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
                return accessToken;
            }
        };
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(securityProperties.getBase64Secret());
        return accessTokenConverter;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }


}

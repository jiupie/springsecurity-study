package com.wl.oauth2.config;

import com.wl.oauth2.config.bean.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Configuration
//使能oauth2 server
@EnableAuthorizationServer
public class AuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter {

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
     * 配置client 客户端信息
     *
     * @param clients /
     * @throws Exception /
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //配置clientId
                .withClient("test")
                //认证类型
                .authorizedGrantTypes("refresh_token", "authorization_code")
                //token有效时间
                .accessTokenValiditySeconds(60 * 60)
                //refresh token有效时间
                .refreshTokenValiditySeconds(60 * 60 * 2)
                //授权范围
                .scopes("all")
                //回调地址
                .redirectUris("http://www.baidu.com")
//                .resourceIds("")
                //秘钥
                .secret(passwordEncoder.encode("sdfadsxcxzcdsfasdfd"))
                //是否自动授权
                .autoApprove(true);
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
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter());
    }


    /**
     * jwt
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    private DefaultTokenServices defaultTokenServices(){
        //可以自定义实现，实现只允许只有用户只能在一个地方登录等等
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());

        //支持刷新token
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);

        defaultTokenServices.setClientDetailsService(memoryClientDetailsService());

        return defaultTokenServices;
    }

    private ClientDetailsService memoryClientDetailsService() {
        //把他加入redis缓存中加快速度


        return null;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();

        DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter) accessTokenConverter.getAccessTokenConverter();

        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userDetailsService);
        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);

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

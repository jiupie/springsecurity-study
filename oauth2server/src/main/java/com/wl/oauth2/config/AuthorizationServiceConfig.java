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
//??????oauth2 server
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
     * ??????????????????
     */
    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;

    /**
     * ??????client ???????????????
     *
     * @param clients /
     * @throws Exception /
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                //??????clientId
//                .withClient("test")
//                //????????????
//                .authorizedGrantTypes("refresh_token", "authorization_code")
//                //token????????????
//                .accessTokenValiditySeconds(60 * 60)
//                //refresh token????????????
//                .refreshTokenValiditySeconds(60 * 60 * 2)
//                //????????????
//                .scopes("all")
//                //????????????
//                .redirectUris("http://www.baidu.com")
////                .resourceIds("")
//                //??????
//                .secret(passwordEncoder.encode("sdfadsxcxzcdsfasdfd"))
//                //??????????????????
//                .autoApprove(true);
        clientDetailsService.setSelectClientDetailsSql(Oauth2Constant.SELECT_CLIENT_DETAIL_SQL);
        clientDetailsService.setDeleteClientDetailsSql(Oauth2Constant.FIND_CLIENT_DETAIL_SQL);
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * ???????????? ??????
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //??????token ??????
                .tokenStore(tokenStore())
                //userDetails ????????????????????????????????????????????????????????????
                .userDetailsService(userDetailsService)
                //?????????????????????
                .authorizationCodeServices(redisAuthenticationCodeService)

                //????????????
                .authenticationManager(authenticationManager);

        endpoints
                //??????tokenservice
                .tokenServices(defaultTokenServices())
                .accessTokenConverter(jwtAccessTokenConverter());

    }


    /**
     * jwt redis??????token
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
//        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    private DefaultTokenServices defaultTokenServices() {
        //????????????????????????????????????????????????????????????????????????????????????
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());

        //????????????token
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);

        //???????????????????????????
        defaultTokenServices.setClientDetailsService(clientDetailsService);

        //??????token?????????
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);


        return defaultTokenServices;
    }

    /**
     * ??????token ????????????????????????token???
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
                //accessToken???????????????
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

package com.wl.oauth2.config.bean;

import lombok.Data;

@Data
public class SecurityProperties {

    /**
     * token在请求头中name名称
     */
    private String header;
    /**
     * 令牌前缀
     */
    private String tokenStartWith;

    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间  毫秒
     */
    private Long tokenValidityInSeconds;
    /**
     * token 续期检查
     */
    private Long detect;

    /**
     * 续期时间
     */
    private Long renew;
}


package com.wl.oauth2.common;

/**
 * 认证所需常量
 */
public class Oauth2Constant {

    /**
     * 12小时，客户端在redis中的时间
     */
    public static final long CLIENT_TIME = 12 * 60;

    /**
     * jwt中用户ID
     */
    public static final String USER_ID = "user_id";

    /**
     * jwt中用户名称
     */
    public static final String USER_NAME = "user_name";

    /**
     * 自定义客户端表名称
     */
    public static String CLIENT_TABLE = "sys_client";


    /**
     * 基础查询语句
     */
    public static final String CLIENT_BASE = "select client_id,client_secret as client_secret, resource_ids, scope, " +
            "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity," +
            "refresh_token_validity, additional_information, autoapprove from " + CLIENT_TABLE;

    public static final String SELECT_CLIENT_DETAIL_SQL = CLIENT_BASE + " where client_id = ?";

    public static final String FIND_CLIENT_DETAIL_SQL = CLIENT_BASE + " order by client_id";

}

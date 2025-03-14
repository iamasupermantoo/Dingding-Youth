package com.youshi.zebra.core.utils;

import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public final class HttpClientHolder {

    public static final int CONNECTION_TIMEOUT = 30000;

    public static final int CON_TIME_OUT_MS = 30000;

    public static final int SO_TIME_OUT_MS = 30000;

    public static final int MAX_CONNECTIONS_PER_HOST = 20;

    public static final int MAX_TOTAL_CONNECTIONS = 200;

    private volatile static HttpClient httpClient;

    public static final HttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (HttpClientHolder.class) {
                if (httpClient == null) {
                    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
                    connectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_HOST);
                    connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
                    connectionManager.setDefaultSocketConfig(SocketConfig.custom()
                            .setTcpNoDelay(true).setSoTimeout(SO_TIME_OUT_MS).build());

                    HttpClientBuilder builder = HttpClientBuilder
                            .create()
                            .setConnectionManager(connectionManager)
                            .setDefaultRequestConfig(
                                    RequestConfig.custom()
                                            .setCookieSpec(CookiePolicy.IGNORE_COOKIES)
                                            .setConnectTimeout(CONNECTION_TIMEOUT)
                                            .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                                            .build());
                    httpClient = builder.build();
                }
            }
        }
        return httpClient;
    }
}

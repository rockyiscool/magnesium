package com.rc.magnesium.resource

import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.rules.ExternalResource

class HttpClientResourceRule extends ExternalResource {

    static final int TIMEOUT = 10
    static final RequestConfig HTTP_CONFIG = RequestConfig.custom()
            .setConnectTimeout(TIMEOUT * 1000)
            .setConnectionRequestTimeout(TIMEOUT * 1000)
            .setSocketTimeout(TIMEOUT * 1000).build()

    def client

    @Override
    void before() throws Throwable {
        this.client = HttpClientBuilder.create()
                .setDefaultRequestConfig(HTTP_CONFIG).build()
    }

    @Override
    void after() {
        try {
            client.close()
        }
        catch (IOException e) {
            e.printStackTrace()
        }
    }
}


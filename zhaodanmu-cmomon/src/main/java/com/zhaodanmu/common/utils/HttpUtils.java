package com.zhaodanmu.common.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class HttpUtils {

    private static final MediaType MEDIA_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient httpClient = new OkHttpClient()
            .newBuilder()
            .connectionPool(new ConnectionPool(3,1, TimeUnit.MINUTES))
            .retryOnConnectionFailure(false)
            .build();

    private HttpUtils() {

    }


    public static String postJSON(String url, Object object) throws Exception {

        RequestBody body = RequestBody.create(MEDIA_JSON, JSON.toJSONString(object));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }


    public static String get(String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
}

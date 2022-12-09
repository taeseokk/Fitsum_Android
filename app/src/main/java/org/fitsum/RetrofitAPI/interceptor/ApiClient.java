package org.fitsum.RetrofitAPI.interceptor;

import org.fitsum.config.AccessTokenSharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request()
                .newBuilder()
                .addHeader("X-AUTH-ACCESS-TOKEN", AccessTokenSharedPreferences.getAccessToken("1"))
                .addHeader("X-AUTH-REFRESH-TOKEN", AccessTokenSharedPreferences.getRefreshToken("1"))
                .build();

        Response response = chain.proceed(request);

        if(response.header("X-AUTH-ACCESS-TOKEN") != null)
            AccessTokenSharedPreferences.setAccessToken(response.header("X-AUTH-ACCESS-TOKEN"));
        return response;
    }
}
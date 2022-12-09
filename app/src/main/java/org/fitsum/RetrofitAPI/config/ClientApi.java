package org.fitsum.RetrofitAPI.config;

import org.fitsum.config.AccessTokenSharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ClientApi implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        if((original.url().encodedPath().equalsIgnoreCase("/api/signup") && original.method().equalsIgnoreCase("post"))
            || (original.url().encodedPath().equalsIgnoreCase("/api/check-id/**") && original.method().equalsIgnoreCase("get"))
            || (original.url().encodedPath().equalsIgnoreCase("/api/check-nickname/**") && original.method().equalsIgnoreCase("get"))){
            return chain.proceed(original);
        }
        Request request = original.newBuilder()
                .header("X-AUTH-ACCESS-TOKEN", AccessTokenSharedPreferences.getAccessToken("1"))
                .header("X-AUTH-REFRESH-TOKEN", AccessTokenSharedPreferences.getRefreshToken("2"))
                .method(original.method(), original.body())
                .build();
        Response response = chain.proceed(request);
        if(response.header("X-AUTH-ACCESS-TOKEN") != null)
            AccessTokenSharedPreferences.setAccessToken(response.header("X-AUTH-ACCESS-TOKEN"));
        return response;
    }
}

package org.fitsum.API;

import org.fitsum.Dto.CommonResult;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AutoLoginAPI {
    @GET("/api/auto-login/")
    Call<CommonResult> autoLogin();
}

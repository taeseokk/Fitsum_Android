package org.fitsum.API;

import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SignUpAPI {

    @POST("/api/signup")
    Call<CommonResult> signup(@Body UserDto.SignupUserDto signupUserDto);

    @GET("/api/check-id/{userId}")
    Call<CommonResult> checkId(@Path("userId") String userId);

    @GET("/api/check-nickname/{nickName}")
    Call<CommonResult> checkNickName(@Path("nickName") String nickName);
}

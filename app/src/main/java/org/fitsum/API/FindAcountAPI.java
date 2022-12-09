package org.fitsum.API;


import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;
import org.fitsum.Dto.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FindAcountAPI {

    @GET("/api/accounts/find-id")
    Call<SingleResult> findAcount(@Query("name") String name, @Query("email") String email);

    @POST("/api/accounts/find-pw")
    Call<CommonResult> findPw(@Body UserDto.FindPwDto findPwDto);
}

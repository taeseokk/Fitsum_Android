package org.fitsum.API;

import org.fitsum.Dto.CommonResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SignInAPI {

    @GET("/api/signin")
    Call<CommonResult> getLogin(@Query("id") String id, @Query("password") String password);

}

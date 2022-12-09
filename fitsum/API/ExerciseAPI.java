package org.fitsum.API;

import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ExerciseAPI {

    @GET("/api/exercise/pushup")
    Call<CommonResult> pushup(@Query("pushup")String pushup);

    @GET("/api/exercise/squart")
    Call<CommonResult> squart(@Query("squart")String squart);

}

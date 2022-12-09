package org.fitsum.API;

import org.fitsum.Dto.CommonResult;

import retrofit2.Call;
import retrofit2.http.POST;

public interface WithdrawalAPI {

    @POST("/api/secession/")
    Call<CommonResult> withdrawal();

}

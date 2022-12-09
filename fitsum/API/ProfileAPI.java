package org.fitsum.API;

import androidx.annotation.NonNull;


import org.fitsum.Dto.BadgeDto;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ProfileAPI {

    //frag 4 닉네임 받아올때
    @GET("/api/profile/mypage")
    Call<SingleResult> getNickname();

    //frag 4 뱃지 받아올 때
    @GET("/api/profile/badge")
    Call<SingleResult> getBadge();

    //frag 1 유저 옷상태 받아올 때
    @GET("/api/profile/item")
    Call<SingleResult> getUser_item();

    //frag 1 코인 갯수 받아올 때
    @GET("/api/profile/coin")
    Call<SingleResult> getUser_coin();

    @GET("/api/profile/coinAndDay")
    Call<SingleResult> getCoinAndDay();


}

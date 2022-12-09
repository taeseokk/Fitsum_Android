package org.fitsum.API;

import org.fitsum.Dto.BadgeDto;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface BadgeAPI {

    //posenetActivity 뱃지 2번(첫 운동) 활성화 시키기
    @PUT("/api/profile/badge2")
    Call<CommonResult> badge2(@Body BadgeDto.ChangeBadge2 changeBadge2);

    //coinActivity 뱃지 3번(아이템 첫 구매) 활성화 시키기
    @PUT("/api/profile/badge3")
    Call<CommonResult> badge3(@Body BadgeDto.ChangeBadge3 changeBadge3);

    //mainActivity 뱃지 생성
    @POST("/api/profile/createbadge")
    Call<SingleResult> createbadge(@Body BadgeDto.CreateBadgeDto createBadgeDto);

    //mainActivity 뱃지테이블 확인
    @GET("/api/profile/checkbadge")
    Call<CommonResult> checkBadge();

    //frag 4 뱃지 받아올 때
    @GET("/api/profile/badge")
    Call<SingleResult> getBadge();
}
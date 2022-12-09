package org.fitsum.API;

import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;
import org.fitsum.Dto.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChangeAPI {
    @PUT("/api/accounts/password")
    Call<CommonResult> updatePw(@Body UserDto.ChangePwDto changePwDto);

    @PUT("/api/accounts/nickname")
    Call<CommonResult> updateNickname(@Body UserDto.ChangenickNameDto changenickNameDto);

    @PUT("/api/accounts/item")
    Call<SingleResult> updateUserItem(@Body UserDto.ChangeUserItemDto changeUserItemDto);

    @PUT("/api/accounts/coin")
    Call<SingleResult> updateUserCoin(@Body UserDto.ChangeUserCoinDto changeUserCoinDto);

    
    //인트형을 변수 단위로 보내고 싶을때 쿼리를 씀
    //@Query("curUserItem")Integer curUserItem,@Query("newUserItem")Integer newUserItem
}

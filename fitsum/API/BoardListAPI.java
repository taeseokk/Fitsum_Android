package org.fitsum.API;

import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BoardListAPI {

    @GET("/api/board-list/everyone")
    Call<SingleResult> showBoardList();

    @GET("/api/board-list/mine")
    Call<SingleResult> showMyBoardList();

    @POST("/api/board/like")
    Call<CommonResult> updateLike(@Query("like") boolean liked, @Query("boardId") Long boardId);

}

package org.fitsum.API;

import org.fitsum.Dto.BoardDto;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BoardAPI {

    @POST("/api/board")
    Call<CommonResult> createBoard(@Body BoardDto.BoardCreateDto boardCreateDto);

    @GET("/api/board")
    Call<SingleResult> showBoard();

    @PUT("/api/board/{board-Id}")
    Call<CommonResult> updateDiary(@Path("board-Id")String boardId);

    @DELETE("/api/board/{board-Id}")
    Call<CommonResult> deleteDiary(@Path("board-Id")String boardId);

    @POST("/api/board/like")
    Call<CommonResult> updateLike(@Query("like") boolean liked, @Query("boardId") Long boardId);


}

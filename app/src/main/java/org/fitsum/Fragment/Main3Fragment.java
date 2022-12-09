package org.fitsum.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.BoardAPI;
import org.fitsum.API.BoardListAPI;
import org.fitsum.BoardActivity;
import org.fitsum.Dto.BoardDto;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main3Fragment extends Fragment {

    private ImageButton write;

    private Main3RecyclerViewAdapter main3RecyclerViewAdapter;
    private Context context;

    private RecyclerView recyclerView;                  //리사이클러뷰
    private NestedScrollView nestedScrollView;          //스크롤
    private ProgressBar progressBar;                    //동그랗게 돌아가는 바

    private int page = 1, limit = 10;

    List<BoardDto.BoardViewDto> boardViewDtoList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main3, container, false);

        context = container.getContext();
        //글 쓰러 가는 버튼 (BoardAcitivty)
        write = (ImageButton) view.findViewById(R.id.write);

        //리사이클러 뷰 관련 xml 요소들 매핑
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.scroll_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.afrag3_rv);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        main3RecyclerViewAdapter = new Main3RecyclerViewAdapter(getContext(), boardViewDtoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(main3RecyclerViewAdapter);

        recyclerView.setHasFixedSize(true);

        //게시판 정보들 불러오기
        getData();

        //게시판 쓰러가기 버튼 누르면 게시판 액티비티 열기
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), BoardActivity.class));
            }
        });


        //리사이클러 뷰를 품고 있는 nestedscrollview 끝에 닿으면 추가로 받아오는 부분
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    getData();
                }
            }
        });

        return view;
    }

    //게시판 데이터 받아오는 메소드
    public void getData(){
        BoardListAPI boardlistAPI = RetrofitBuilder.getRetrofit().create(BoardListAPI.class);

        boardlistAPI.showBoardList().enqueue(new Callback<SingleResult>() {
            @Override
            public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                if(response.isSuccessful() && response.body() != null){
                    progressBar.setVisibility(View.GONE);
                    SingleResult result = response.body();

                    parseResult(result);
                }
            }

            @Override
            public void onFailure(Call<SingleResult> call, Throwable t) {
                Log.e("에러 : ", t.getMessage());
            }
        });

    }

    //JSON 이용하여 데이터 가져오기
    private void parseResult(SingleResult result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<BoardDto.BoardViewDto>>(){
        }.getType();
        String jsonResult = gson.toJson(result.getData());
        Log.d("test", jsonResult);
        //53번째 줄에서 만든 List<BoardDto.BoardViewDto> 형식의 배열에 데이터 저장.
        boardViewDtoList = gson.fromJson(jsonResult, type);

        //recyclerView 에 main3RecyclerViewAdapter 클래스 연결
        recyclerView.setAdapter(main3RecyclerViewAdapter);
    }


}
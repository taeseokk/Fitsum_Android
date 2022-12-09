package org.fitsum.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.ChangeAPI;
import org.fitsum.API.ProfileAPI;
import org.fitsum.CalenderActivity;
import org.fitsum.Dto.BadgeDto;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;
import org.fitsum.Dto.UserDto;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main2Fragment extends Fragment {

    private Button calender, daycheck;
    private String Day1, Day2, coin_s;
    private Boolean isCheckedday = false;
    private Integer coin;
    TextView day_quest1, day_quest2;

    private String[] DayQuest = new String[5];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main2, container, false);

        daycheck = (Button) view.findViewById(R.id.daycheck);

        ProfileAPI profileAPI = RetrofitBuilder.getRetrofit().create(ProfileAPI.class);
        ChangeAPI changeAPI = RetrofitBuilder.getRetrofit().create(ChangeAPI.class);

        UserDto.ChangeUserCoinDto changeUserCoinDto = new UserDto.ChangeUserCoinDto();      //유저 코인 증가시 필요

        profileAPI.getUser_coin().enqueue(new Callback<SingleResult>() {
            @Override
            public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                if(response.isSuccessful()){
                    SingleResult data = response.body();
                    coin_s = data.getData().toString();             //coin_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                    coin = (int)Double.parseDouble(coin_s);         //coin 은 그것을 받아서 정수형으로 바꿔주는 역할.
                }
            }

            @Override
            public void onFailure(Call<SingleResult> call, Throwable t) {

            }
        });


        daycheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckedday){
                    changeUserCoinDto.setCurUserCoin(coin);
                    changeUserCoinDto.setNewUserCoin(coin + 30);
                    //유저의 코인증가
                    changeAPI.updateUserCoin(changeUserCoinDto).enqueue(new Callback<SingleResult>() {
                        @Override
                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                            if (response.isSuccessful()) {
                                CommonResult data = response.body();

                                if (data.getCode() == 201) {
                                    //출석체크 하고 나서 코인 올리고 true로 바꿈
                                    isCheckedday = true;
                                    Toast.makeText(getActivity(),"출석체크 코인 30개를 드립니다.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SingleResult> call, Throwable t) {

                        }
                    });

                }
                else {
                    Toast.makeText(getActivity(),"이미 출석체크 하였습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Random rand = new Random();
        DayQuest[0] = "푸쉬업 50개";
        DayQuest[1] = "스쿼트 50개";


        day_quest1 = view.findViewById(R.id.day_quest1);
        day_quest2 = view.findViewById(R.id.day_quest2);

//        int random1 = 0, random2=0;
//
//        random1 = rand.nextInt(5);
//        random2 = rand.nextInt(5);
//        if(random1==random2){
//            if(random1 !=0)
//                random2 = (random1 - 1);
//            else
//                random2 = random1 +rand.nextInt(4);
//        }

        day_quest1.setText(DayQuest[0]);
        day_quest2.setText(DayQuest[1]);


        calender = (Button) view.findViewById(R.id.calender);
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CalenderActivity.class);
                startActivity(intent);
                getActivity();
            }
        });

        return view;

    }
}
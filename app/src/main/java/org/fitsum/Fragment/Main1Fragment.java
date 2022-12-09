package org.fitsum.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.ProfileAPI;
import org.fitsum.CoinActivity;
import org.fitsum.Dto.SingleResult;
import org.fitsum.Dto.UserDto;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;
import org.fitsum.posenet.CameraActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main1Fragment extends Fragment {

    private ImageButton ib_exercise;
    ImageView character;
    //    ImageButton alram;
    private String item_s, coin_s;
    private TextView coin_num;
    private Integer item, coin;

    UserDto.SignupUserDto signupUserDto = new UserDto.SignupUserDto();
    ProfileAPI profileAPI = RetrofitBuilder.getRetrofit().create(ProfileAPI.class);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main1, container, false);

        character = (ImageView) view.findViewById(R.id.character);
        coin_num = (TextView) view.findViewById(R.id.coin_num);

        //유저의 옷상태에 맞춰 이미지를 띄어주기
        //메인 1번에 올때마다 실행해야함.
        change_item();

        //코인값 받아오기
        profileAPI.getUser_coin().enqueue(new Callback<SingleResult>() {
            @Override
            public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                if (response.isSuccessful()) {
                    SingleResult data = response.body();
                    coin_s = data.getData().toString();             //coin_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                    coin = (int)Double.parseDouble(coin_s);         //coin 은 그것을 받아서 정수형으로 바꿔주는 역할.

                    Log.d("test", String.valueOf(data.getData()));
                    Log.d("data", String.valueOf(data));

                    coin_num.setText("" + coin);
                }
            }

            @Override
            public void onFailure(Call<SingleResult> call, Throwable t) {

            }
        });

        //상체 운동 - 푸쉬업
        ib_exercise = (ImageButton) view.findViewById(R.id.ib_exercise);
        ib_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), CameraActivity.class));
            }
        });

        //상점
        coin_num = (TextView) view.findViewById(R.id.coin_num);
        coin_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), CoinActivity.class));
            }
        });

        //알림
//        alram = (ImageButton) view.findViewById(R.id.alram);
//        alram.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().startActivity(new Intent(getActivity(), AlramActivity.class));
//            }
//        });


        return view;

    }

    //처음화면의 캐릭터 설정
    private void change_item() {
        //현재 유저의 옷 정보를 받아와서 그에 맞는 이미지 대입.
        profileAPI.getUser_item().enqueue(new Callback<SingleResult>() {
            @Override
            public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                if (response.isSuccessful()) {
                    SingleResult data = response.body();
                    item_s = data.getData().toString();             //item_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                    item = (int)Double.parseDouble(item_s);         //item 은 그것을 받아서 정수형으로 바꿔주는 역할.

                    Log.d("item", String.valueOf(item));

                    //아이템
                    if(item==1){
                        character.setImageResource(R.drawable.man_1);
                    }
                    else if(item==2){
                        character.setImageResource(R.drawable.man_2);
                    }
                    else if(item==3){
                        character.setImageResource(R.drawable.man_3);
                    }
                    else if(item==4){
                        character.setImageResource(R.drawable.man_4);
                    }
                    else if(item==5){
                        character.setImageResource(R.drawable.man_5);
                    }
                    else if(item==6){
                        character.setImageResource(R.drawable.man_6);
                    }
                    else if(item==7){
                        character.setImageResource(R.drawable.man_7);
                    }
                    else if(item==8){
                        character.setImageResource(R.drawable.man_8);
                    }
                    else if(item==9){
                        character.setImageResource(R.drawable.man_9);
                    }
                    else if(item==10) {
                        character.setImageResource(R.drawable.woman_10);
                    }
                    else if(item==11) {
                        character.setImageResource(R.drawable.woman_11);
                    }
                    else if(item==12) {
                        character.setImageResource(R.drawable.woman_12);
                    }
                    else if(item==13) {
                        character.setImageResource(R.drawable.woman_13);
                    }
                    else if(item==14) {
                        character.setImageResource(R.drawable.woman_14);
                    }
                    else if(item==15) {
                        character.setImageResource(R.drawable.woman_15);
                    }
                    else if(item==16) {
                        character.setImageResource(R.drawable.woman_16);
                    }
                    else if(item==17) {
                        character.setImageResource(R.drawable.woman_17);
                    }
                    else if(item==18) {
                        character.setImageResource(R.drawable.woman_18);
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResult> call, Throwable t) {

            }
        });

    }


}


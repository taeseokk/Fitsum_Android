package org.fitsum;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.ChangeAPI;
import org.fitsum.API.SignUpAPI;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.UserDto;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeNicknameActivity extends AppCompatActivity {
    EditText et_cur_nickname, et_new_nickname;
    Button btn_change_nickname,back, btn_nickname_duplicate_check;
    boolean isCheckedNickName;
    String nickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname_activiy);

        et_cur_nickname = findViewById(R.id.et_cur_nickname);
        et_new_nickname = findViewById(R.id.et_new_nickname);
        btn_change_nickname = findViewById(R.id.btn_change_nickname);
        btn_nickname_duplicate_check = findViewById(R.id.btn_nickname_duplicate_check);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_nickname_duplicate_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpAPI signUpAPI = RetrofitBuilder.getRetrofit().create(SignUpAPI.class);
                nickname = et_new_nickname.getText().toString();

                Log.d("test", "여기까진 잘 되네요");

                signUpAPI.checkNickName(nickname).enqueue(new Callback<CommonResult>() {
                    @Override
                    public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                        if(response.isSuccessful()){
                            CommonResult data = response.body();

                            if(data.getCode()==201){
                                Toast.makeText(getApplicationContext(),"확인되었습니다.", Toast.LENGTH_SHORT).show();
                                isCheckedNickName=true;
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();
                                isCheckedNickName=false;
                            }
                            Log.d("test", data.getMsg());
                        }
                    }
                    @Override
                    public void onFailure(Call<CommonResult> call, Throwable t) {
                        Log.d("tag", "실패입니다.");
                    }
                });
            }
        });


        btn_change_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAPI changeAPI = RetrofitBuilder.getRetrofit().create(ChangeAPI.class);

                String curnickName = et_cur_nickname.getText().toString();
                String newnickName = et_new_nickname.getText().toString();


                if(isCheckedNickName == true) {

                    UserDto.ChangenickNameDto ChangenickNameDto = new UserDto.ChangenickNameDto();

                    ChangenickNameDto.setcurnickName(curnickName);
                    ChangenickNameDto.setnewnickName(newnickName);

                    changeAPI.updateNickname(ChangenickNameDto).enqueue(new Callback<CommonResult>() {
                        @Override
                        public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                            if(response.body().getCode() == 201){
                                Toast.makeText(getApplicationContext(), "닉네임 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }else if(response.body().getCode() == 17)
                                Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<CommonResult> call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(getApplicationContext(), "통신에 에러가 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                    Toast.makeText(getApplicationContext(), "닉네임이 중복입니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
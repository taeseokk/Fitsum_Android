package org.fitsum.FirstPart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.fitsum.API.FindAcountAPI;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.UserDto;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.miguelrochefort.fitnesscamera.R;

public class FindPwActivity extends AppCompatActivity {

    private EditText et_id, et_name, et_email;
    private Button find_pw, back;
    private String id, email, username;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);


        et_id = findViewById(R.id.et_id);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        find_pw = findViewById(R.id.find_pw);
        find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindAcountAPI findAcountAPI = RetrofitBuilder.getRetrofit().create(FindAcountAPI.class);

                id = et_id.getText().toString();
                username = et_name.getText().toString();
                email = et_email.getText().toString();

                UserDto.FindPwDto findPwDto = new UserDto.FindPwDto();

                findPwDto.setEmail(email);
                findPwDto.setUserName(username);
                findPwDto.setUserId(id);

                findAcountAPI.findPw(findPwDto).enqueue(new Callback<CommonResult>() {
                    @Override
                    public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                        if (response.body().getCode() == 201) {
                            Toast.makeText(getApplicationContext(), "이메일로 임시 비밀번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            Toast.makeText(getApplicationContext(), "계정을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<CommonResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "통신 오류입니다.", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        });

    }

}

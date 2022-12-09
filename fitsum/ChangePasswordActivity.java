package org.fitsum;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.ChangeAPI;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.UserDto;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText et_cur_password, et_new_password, et_check_password;
    Button btn_change_pw,back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_activiy);

        et_cur_password = findViewById(R.id.et_cur_nickname);
        et_new_password = findViewById(R.id.et_new_nickname);
        et_check_password = findViewById(R.id.et_check_password);
        btn_change_pw = findViewById(R.id.btn_change_nickname);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAPI changeAPI = RetrofitBuilder.getRetrofit().create(ChangeAPI.class);

                String curPw = et_cur_password.getText().toString();
                String newPw = et_new_password.getText().toString();
                String checkPw = et_check_password.getText().toString();

                if(newPw.equals(checkPw)) {

                    UserDto.ChangePwDto changePwDto = new UserDto.ChangePwDto();

                    changePwDto.setCurPw(curPw);
                    changePwDto.setNewPw(newPw);

                    changeAPI.updatePw(changePwDto).enqueue(new Callback<CommonResult>() {
                        @Override
                        public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                            if(response.body().getCode() == 201){
                                Toast.makeText(getApplicationContext(), "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}

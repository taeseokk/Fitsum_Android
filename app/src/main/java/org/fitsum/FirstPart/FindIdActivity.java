package org.fitsum.FirstPart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.miguelrochefort.fitnesscamera.R;
import org.fitsum.API.FindAcountAPI;
import org.fitsum.Dto.SingleResult;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindIdActivity extends AppCompatActivity {

    private EditText et_name, et_email;
    private Button find_id,back;
    private String name, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        et_name=findViewById(R.id.et_name);
        et_email=findViewById(R.id.et_email);

        find_id=findViewById(R.id.find_id);

        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        find_id=findViewById(R.id.find_id);
        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindAcountAPI findAcountAPI = RetrofitBuilder.getRetrofit().create(FindAcountAPI.class);
                name = et_name.getText().toString();
                email = et_email.getText().toString();

                findAcountAPI.findAcount(name, email).enqueue(new Callback<SingleResult>() {
                    @Override
                    public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                        if (response.isSuccessful()) {
                            //StatusDto 에 응답받은 내용을 저장
                            SingleResult data = response.body();

                            if(data.getCode()==201) {
                                String getName;
                                getName= data.getData().toString();
                                String userId = getName.replaceFirst("\\{userId=", "");
                                String getUserId = userId.replace("}", "");


                                Log.d("tag", getUserId);
                                AlertDialog.Builder builder = new AlertDialog.Builder(FindIdActivity.this);
                                builder.setMessage("아이디는 "+"\"" + getUserId + "\""+" 입니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(FindIdActivity.this, FirstActivity.class);
                                                FindIdActivity.this.finish();
                                                startActivity(i);
                                            }
                                        }).show();


                            }
                            else
                                Toast.makeText(FindIdActivity.this, "찾을수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure (Call < SingleResult > call, Throwable t){
                        Log.d("tag", "실패입니다.");

                    }
                });


            }
        });

    }
}

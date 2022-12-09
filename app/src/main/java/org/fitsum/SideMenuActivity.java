package org.fitsum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.WithdrawalAPI;
import org.fitsum.Dto.CommonResult;
import org.fitsum.FirstPart.FirstActivity;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;
import org.fitsum.config.AccessTokenSharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SideMenuActivity extends AppCompatActivity {

   Button back;
   TextView version, logout, withdrawal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidemenu);

        back = (Button) findViewById(R.id.back);                        //뒤로가기
//        alram = (TextView) findViewById(R.id.alram);                    //알림
//        notice = (TextView) findViewById(R.id.notice);                  //공지사항
        version = (TextView) findViewById(R.id.version);                //버전
        logout = (TextView) findViewById(R.id.logout);                  //로그아웃
        withdrawal = (TextView) findViewById(R.id.withdrawal);          //회원탈퇴


        //뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        //알림 버튼
//        alram.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        //공지사항
//        notice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SideMenuActivity.this, NoticeActivity.class));
//                overridePendingTransition(R.anim.slideout_right_to_left, R.anim.slidein_right_to_left);
//
//            }
//        });

        //로그아웃 버튼
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLogout();
            }
        });

        //회원탈퇴 버튼
        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWithdrawal();
            }
        });
    }


    //로그아웃 다이얼로그
    private void showDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SideMenuActivity.this);
        builder.setMessage("로그아웃 하시겠습니까?")
                .setTitle("Logout")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccessTokenSharedPreferences.setAccessToken(null);
                        AccessTokenSharedPreferences.setRefreshToken(null);

                        Intent i = new Intent(SideMenuActivity.this, FirstActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        finish();
                        startActivity(i);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    //회원탈퇴 버튼 show dialog
    private void showDialogWithdrawal() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SideMenuActivity.this);
        builder.setMessage("회원탈퇴 하시겠습니까?")
                .setTitle("회원탈퇴")
                .setPositiveButton("회원탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        secession();                                                         //회원탈퇴 API
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    //탈퇴 API 이용해서 탈퇴하고 FirstActivity로 가는 메서드
    private void secession() {
        WithdrawalAPI withdrawalAPI = RetrofitBuilder.getRetrofit().create(WithdrawalAPI.class);        //탈퇴 API

        withdrawalAPI.withdrawal().enqueue(new Callback<CommonResult>() {
            @Override
            public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                if(response.isSuccessful()){
                    //이것도 응답에 대한 부분은 StatusDto로 받음
                    CommonResult data = response.body();

                    //201은 성공했을때 코드.
                    if(data.getCode()==201){
                        Intent i = new Intent(SideMenuActivity.this, FirstActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        finish();
                        startActivity(i);
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
}
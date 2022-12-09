package org.fitsum.FirstPart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.SignInAPI;
import org.fitsum.Dto.CommonResult;
import org.fitsum.MainActivity;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;
import org.fitsum.config.AccessTokenSharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private View view;
    private Button btn_find_id, btn_find_pw, btn_login;
    private EditText et_id, et_passwd;
    private String id, password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        SignInAPI signInAPI = RetrofitBuilder.getRetrofit().create(SignInAPI.class);    //SignAPI 클래스와 연결
        AccessTokenSharedPreferences.init(getActivity());                               //AcessToken과 연결
        view = inflater.inflate(R.layout.login_frag, container, false);

        SignInAPI signInAPI = RetrofitBuilder.getRetrofit().create(SignInAPI.class);        //SignInAPI 의 변수 선언 (and 변수에 통신하는헤더를 추가해줌)

        btn_find_id = view.findViewById(R.id.btn_find_id);
        btn_find_pw = view.findViewById(R.id.btn_find_pw);
        btn_login = view.findViewById(R.id.btn_login);

        et_id = view.findViewById(R.id.et_id);
        et_passwd = view.findViewById(R.id.et_passwd);
        et_passwd.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //아이디 찾기 버튼
        btn_find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FindIdActivity.class);
                startActivity(intent);
            }
        });

        //비밀번호 찾기 버튼
        btn_find_pw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FindPwActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 눌렀을 때
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id = et_id.getText().toString();
                password = et_passwd.getText().toString();

                Log.d("login", "여기까진 잘 되네요");

                if(et_id.getText().toString().replace(" ","").equals(""))
                    Toast.makeText(getActivity(),"아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(et_passwd.getText().toString().replace(" ","").equals(""))
                    Toast.makeText(getActivity(),"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                else {
                    signInAPI.getLogin(id, password).enqueue(new Callback<CommonResult>() {
                        @Override
                        public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                            if (response.isSuccessful()) {
                                //StatusDto 에 응답받은 내용을 저장
                                CommonResult data = response.body();
                                Log.d("data log ", String.valueOf(data.getCode()));
                                //로그인하면서 토큰정보 저장
                                AccessTokenSharedPreferences.setAccessToken(response.headers().get("X-AUTH-ACCESS-TOKEN"));
                                AccessTokenSharedPreferences.setRefreshToken(response.headers().get("X-AUTH-REFRESH-TOKEN"));



                                if (data.getCode() == 201) {
                                    //로그인 성공시 메인 액티비티 시작
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                                //로그인 실패시 메시지 띄워주기
                                else if (data.getCode() == 13)
                                    Toast.makeText(getActivity(), "비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(), "존재하지 않는 회원입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResult> call, Throwable t) {
                            Log.d("tag", "실패입니다.");
                            Toast.makeText(getActivity(), "존재하지 않는 회원입니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

        return view;
    }
}

package org.fitsum.FirstPart;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.SignUpAPI;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.UserDto;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private View view;

    private Button btn_id_duplicate_check, btn_nickname_duplicate_check, btn_register;
    private EditText et_name, et_id, et_passwd, et_passwd_check, et_nickname,et_email, et_tall, et_weight;
    private TextView text_check_password;

    private RadioGroup radioGroup;
    private RadioButton rdb_man, rdb_woman;
    public int userSex;

    private boolean isPasswordSame, isCheckedNickName, isCheckedId, isCheckedSex;

    private String id, nickname, password, name, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_frag, container, false);

        SignUpAPI signUpAPI = RetrofitBuilder.getRetrofit().create(SignUpAPI.class);

        //button들
        btn_id_duplicate_check = view.findViewById(R.id.btn_id_duplicate_check);
        btn_nickname_duplicate_check = view.findViewById(R.id.btn_nickname_duplicate_check);
        btn_register = view.findViewById(R.id.btn_register);

        //edit_text들
        et_name = view.findViewById(R.id.et_name);
        et_id = view.findViewById(R.id.et_id);
        et_passwd = view.findViewById(R.id.et_passwd);
        et_passwd_check = view.findViewById(R.id.et_passwd_check);
        et_nickname = view.findViewById(R.id.et_nickname);
        et_email = view.findViewById(R.id.et_email);

        rdb_man=view.findViewById(R.id.rdb_man);
        rdb_woman=view.findViewById(R.id.rdb_woman);



        //비밀번호 체크 변수
        text_check_password = view.findViewById(R.id.text_check_password);

        //email 형식 맞추기
        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    et_email.getText().toString().replace(" ","").equals("");
                    Pattern p = Pattern.compile("^[_a-zA-Z0-9-]+(.[_a-zA-Z0-9-]+)*@(?:\\w+\\.)+\\w+$]");
                    Matcher m = p.matcher((et_email).getText().toString());

                    if(!m.matches()){
                        Toast.makeText(getActivity(), "Email 형식으로 입력하세요.",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        //성별 체크
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {    //이름없는 객체
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {         //체크된 상태에서만 작동되서 실행문만 적으면 됨.
                // 추후 서버에 보낼때 userSex값을 Dto에 추가해서 넘겨야할까 아님 .. 헤더에뭐 그런느낌
                if(rdb_man.isChecked()){
                    userSex = 1;
                    Toast.makeText(getActivity(),"남자",Toast.LENGTH_SHORT).show();
                    isCheckedSex=true;
                }
                else if(rdb_woman.isChecked()){
                    userSex=2;
                    Toast.makeText(getActivity(),"여자",Toast.LENGTH_SHORT).show();
                    isCheckedSex=true;
                }
                else{
                    Toast.makeText(getActivity(),"성별을 다시 골라주세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //비밀번호랑 비밀번호 확인이 다른 경우 알림 띄움.
        et_passwd_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(et_passwd.getText().toString())){
                    text_check_password.setText("비밀번호가 다릅니다. 비밀번호를 확인하세요.");
                    isPasswordSame = false;
                }else{
                    text_check_password.setText("비밀번호가 같습니다.");
                    isPasswordSame = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //id
        btn_id_duplicate_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = et_id.getText().toString();                            //입력받은 아이디를 가져옴

                signUpAPI.checkId(id).enqueue(new Callback<CommonResult>() {       //signupAPI 의 checkId를 이용. -> 값은 id
                    @Override
                    public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                        if(response.isSuccessful()){
                            //이것도 응답에 대한 부분은 StatusDto로 받음
                            CommonResult data = response.body();

                            //201은 성공했을때 코드.
                            if(data.getCode()==201){
                                Toast.makeText(getActivity(),"확인되었습니다.", Toast.LENGTH_SHORT).show();
                                isCheckedId=true;
                            }
                            //그 외의 코드는 성공하지 못한 코드이기 때문에 else로 전부 예외처리
                            else{
                                Toast.makeText(getActivity(),"ID가 중복됩니다.", Toast.LENGTH_SHORT).show();
                                isCheckedId=false;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResult> call, Throwable t) {
                        Toast.makeText(getActivity(),"문제가 있습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //nickname
        btn_nickname_duplicate_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname = et_nickname.getText().toString();

                Log.d("test", "여기까진 잘 되네요");

                signUpAPI.checkNickName(nickname).enqueue(new Callback<CommonResult>() {
                    @Override
                    public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                        if(response.isSuccessful()){
                            CommonResult data = response.body();

                            if(data.getCode()==201){
                                Toast.makeText(getActivity(),"확인되었습니다.", Toast.LENGTH_SHORT).show();
                                isCheckedNickName=true;
                            }
                            else{
                                Toast.makeText(getActivity(),"닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();
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



        //마지막으로 회원가입 눌렀을때 확인
        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                et_email.getText().toString().replace(" ","").equals("");
                Pattern p = Pattern.compile("^[_a-zA-Z0-9-]+(.[_a-zA-Z0-9-]+)*@(?:\\w+\\.)+\\w+$");
                Matcher m = p.matcher((et_email).getText().toString());

                //한번 쭉 다시 중복체크 해주고
                if(et_name.getText().toString().replace(" ","").equals(""))
                    Toast.makeText(getActivity(),"이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(!isCheckedSex)
                    Toast.makeText(getActivity(),"성별을 선택하세요.",Toast.LENGTH_SHORT).show();
                else if(!isCheckedId)
                    Toast.makeText(getActivity(),"ID가 중복체크 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                else if(!isPasswordSame)
                    Toast.makeText(getActivity(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                else if(!isCheckedNickName)
                    Toast.makeText(getActivity(),"닉네임이 중복체크가 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                else if(!m.matches())
                    Toast.makeText(getActivity(), "Email 형식으로 입력하세요.",Toast.LENGTH_SHORT).show();


                //되었다면 else부터 시작
                else{
                    //signupuserDto 를 선언하고
                    UserDto.SignupUserDto signupUserDto = new UserDto.SignupUserDto();

                    id = et_id.getText().toString();
                    password = et_passwd.getText().toString();
                    nickname = et_nickname.getText().toString();
                    name = et_name.getText().toString();
                    email = et_email.getText().toString();

                    Log.d(String.valueOf(userSex),"sex");

                    signupUserDto.setUserId(id);        //해당 Dto에 값을 저장
                    signupUserDto.setUserPw(password);
                    signupUserDto.setNickName(nickname);
                    signupUserDto.setUserName(name);
                    signupUserDto.setEmail(email);
                    signupUserDto.setUserSex(userSex);
                    //성별을 이용하여 아이템의 기본값 저장
                    if(userSex==1) {
                        signupUserDto.setUserItem(userSex);
                    }
                    else if(userSex==2) {
                        signupUserDto.setUserItem(userSex + 8);
                    }


                    //signupAPI 의 checkId를 이용. -> 값은 id
                    signUpAPI.signup(signupUserDto).enqueue(new Callback<CommonResult>() {
                        @Override
                        public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                            if(response.isSuccessful()){

                                CommonResult data = response.body();

                                if(data.getCode()==201){
                                    Toast.makeText(getActivity(),"확인되었습니다.", Toast.LENGTH_SHORT).show();
                                    dismiss();          //dialog 닫기
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


        });

        return view;
    }
}

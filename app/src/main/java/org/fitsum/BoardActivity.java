package org.fitsum;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.BoardAPI;
import org.fitsum.Dto.BoardDto;
import org.fitsum.Dto.CommonResult;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardActivity extends AppCompatActivity {

    private Button back, btn_save;
    private EditText et_title, et_content;
    private String title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        et_content = (EditText) findViewById(R.id.et_content);
        et_title = (EditText) findViewById(R.id.et_title);

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save=(Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        BoardAPI boardAPI = RetrofitBuilder.getRetrofit().create(BoardAPI.class);

        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(BoardActivity.this)
            .setMessage("저장 하시겠습니까?")

                //저장
                .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BoardDto.BoardCreateDto boardCreateDto = new BoardDto.BoardCreateDto();

                        title = et_title.getText().toString();
                        content = et_content.getText().toString();

                        boardCreateDto.setContent(content);
                        boardCreateDto.setTitle(title);

                        Log.d("title",title);
                        Log.d("content", content);

                        boardAPI.createBoard(boardCreateDto).enqueue(new Callback<CommonResult>() {
                            @Override
                            public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                                CommonResult data = response.body();

                                if (data.getCode() == 201) {
                                     Toast.makeText(BoardActivity.this, "확인되었습니다.", Toast.LENGTH_SHORT).show();
                                     finish();
                                }
                                Log.d("test", data.getMsg());


                            }

                            @Override
                            public void onFailure(Call<CommonResult> call, Throwable t) {

                            }
                        });
                    }
                })

                //취소
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(BoardActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();

    }
}
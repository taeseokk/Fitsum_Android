package org.fitsum.FirstPart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.AutoLoginAPI;
import org.fitsum.API.ProfileAPI;
import org.fitsum.Dto.BadgeDto;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;
import org.fitsum.MainActivity;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;
import org.fitsum.config.AccessTokenSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    private View bottomSheetView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        //토큰을 받을 저장공간을 생성ㅇ
        AccessTokenSharedPreferences.init(getApplicationContext());

        AutoLoginAPI autoLoginAPI = RetrofitBuilder.getRetrofit().create(AutoLoginAPI.class);

        autoLoginAPI.autoLogin().enqueue(new Callback<CommonResult>() {
            @Override
            public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                if(response.isSuccessful()){
                    //이것도 응답에 대한 부분은 StatusDto로 받음
                    CommonResult data = response.body();

                    //201은 성공했을때 코드.
                    if(data.getCode()==201){

                        Intent intent=new Intent();
                        intent.setClass(FirstActivity.this, MainActivity.class);
                        startActivity(intent);

                        finish();
                    }

                    Log.d("test", data.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonResult> call, Throwable t) {
                Log.d("tag", "실패입니다.");
            }
        });



        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
        //로그인, 회원가입 버튼
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_register = findViewById(R.id.btn_register);

        //로그인 버튼 누를때
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment loginSheetDialog = new LoginBottomSheetDialogFragment();
                loginSheetDialog.show(getSupportFragmentManager(), "loginBottomSheet");

            }
        });

        //회원가입 버튼 누를때
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment registersheetDialog = new RegisterBottomSheetDialogFragment();
                registersheetDialog.show(getSupportFragmentManager(), "registerBottomSheet");
            }
        });

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
        //슬라이드 이미지
        showMainSlideImages();
    }

    public void showMainSlideImages(){
        viewPager2 = findViewById((R.id.viewPagerImageSlider));

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.image1));
        sliderItems.add(new SliderItem(R.drawable.image2));
        sliderItems.add(new SliderItem(R.drawable.image3));
        sliderItems.add(new SliderItem(R.drawable.image4));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer= new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r =  1- Math.abs(position);
                page.setScaleY(0.95f + r * 0.2f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 6000);        //사진 자동 넘김 시간

            }
        });

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}

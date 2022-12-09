package org.fitsum;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;
import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.BadgeAPI;
import org.fitsum.API.ProfileAPI;
import org.fitsum.Dto.BadgeDto;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;
import org.fitsum.Dto.UserDto;
import org.fitsum.Fragment.Main1Fragment;
import org.fitsum.Fragment.Main2Fragment;
import org.fitsum.Fragment.Main3Fragment;
import org.fitsum.Fragment.Main4Fragment;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Main1Fragment main1Fragment;
    Main2Fragment main2Fragment;
    Main3Fragment main3Fragment;
    Main4Fragment main4Fragment;

    private Boolean isCheckedBadge;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        main1Fragment = new Main1Fragment();
        main2Fragment = new Main2Fragment();
        main3Fragment = new Main3Fragment();
        main4Fragment = new Main4Fragment();


        BadgeAPI badgeAPI = RetrofitBuilder.getRetrofit().create(BadgeAPI.class);
        BadgeDto.CreateBadgeDto createBadgeDto = new BadgeDto.CreateBadgeDto();

        //유저 뱃지가 만들어져있는지 확인하고 없다면 만들기
        badgeAPI.checkBadge().enqueue(new Callback<CommonResult>() {

            @Override
            public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                if (response.isSuccessful()) {
                    CommonResult data = response.body();

                    //체크 뱃지시 없는게 확인된다면 크리에이트
                    if(data.getCode()==201) {
                        badgeAPI.createbadge(createBadgeDto).enqueue(new Callback<SingleResult>() {
                            @Override
                            public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                if (response.isSuccessful()) {

                                    SingleResult data = response.body();

                                    Log.d("create Badge", data.getMsg());
                                }
                            }

                            @Override
                            public void onFailure(Call<SingleResult> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResult> call, Throwable t) {

            }
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.containers, main1Fragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.main_menu);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){

                switch(item.getItemId()){
                    case R.id.main1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, main1Fragment).commit();
                        return true;
                    case R.id.main2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, main2Fragment).commit();
                        return true;
                    case R.id.main3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, main3Fragment).commit();
                        return true;
                    case R.id.main4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, main4Fragment).commit();
                        return true;
                }
                return false;
            }
        });

    }



}
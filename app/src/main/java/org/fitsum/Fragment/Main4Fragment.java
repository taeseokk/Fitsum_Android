package org.fitsum.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelrochefort.fitnesscamera.R;
import org.fitsum.API.BadgeAPI;

import org.fitsum.API.ProfileAPI;
import org.fitsum.Dto.BadgeDto;
import org.fitsum.Dto.SingleResult;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;
import org.fitsum.SideMenuActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main4Fragment extends Fragment {

    ImageButton menu ,badge_1, badge_2, badge_3, badge_4, badge_5, badge_6;
    TextView profile;

    private TextView profile_name;
    private String nickname;
    private Boolean badge1 =false;
    private Boolean badge2=false;
    private Boolean badge3=false;
    private Boolean badge4=false;
    private Boolean badge5=false;
    private Boolean badge6=false;


    List<BadgeDto.BadgeViewDto> badgeViewDtoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main4, container, false);

        BadgeAPI badgeAPI = RetrofitBuilder.getRetrofit().create(BadgeAPI.class);
        ProfileAPI profileAPI = RetrofitBuilder.getRetrofit().create(ProfileAPI.class);



        menu =(ImageButton) view.findViewById(R.id.menu);
        badge_1 =(ImageButton) view.findViewById(R.id.badge_1);
        badge_2 =(ImageButton) view.findViewById(R.id.badge_2);
        badge_3 =(ImageButton) view.findViewById(R.id.badge_3);
        badge_4 =(ImageButton) view.findViewById(R.id.badge_4);
        badge_5 =(ImageButton) view.findViewById(R.id.badge_5);
        badge_6 =(ImageButton) view.findViewById(R.id.badge_6);
        profile_name = (TextView) view.findViewById(R.id.profile_name);

        //????????? ????????????
        profileAPI.getNickname().enqueue(new Callback<SingleResult>() {
            @Override
            public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                if(response.isSuccessful()){
                    SingleResult data = response.body();
                    nickname = data.getData().toString();
                    profile_name.setText(nickname);
                }
            }
            @Override
            public void onFailure(Call<SingleResult> call, Throwable t) {
                t.printStackTrace();
            }
        });



        //?????? ????????????   (1 ~ 6 ???????????? ????????? true ????????? false ????????? ????????????)
        //???????????? true ????????? ?????? ????????? ?????????????????????.

        badgeAPI.getBadge().enqueue(new Callback<SingleResult>() {

            @Override
            public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                if(response.isSuccessful() && response.body() != null) {
                    SingleResult data = response.body();

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<BadgeDto.BadgeViewDto>>(){
                    }.getType();
                    String jsonResult = gson.toJson(data.getData());

                    badgeViewDtoList = gson.fromJson(jsonResult, type);


                    Log.d("badgeviewlist", String.valueOf(badgeViewDtoList));
                    Log.d("badgeviewlist[0]", String.valueOf(badgeViewDtoList.get(0)));

                    badge1=badgeViewDtoList.get(0).isBadge1();
                    badge2=badgeViewDtoList.get(0).isBadge2();
                    badge3=badgeViewDtoList.get(0).isBadge3();
                    badge4=badgeViewDtoList.get(0).isBadge4();
                    badge5=badgeViewDtoList.get(0).isBadge5();
                    badge6=badgeViewDtoList.get(0).isBadge6();


                }
            }
            @Override
            public void onFailure(Call<SingleResult> call, Throwable t) {

            }
        });

        //??????


        //????????? ????????? ?????? true ?????? ????????? ???????????? ??????.
        if(badge1)
            badge_1.setBackgroundResource(R.drawable.badge_background_complete);
        if(badge2)
            badge_2.setBackgroundResource(R.drawable.badge_background_complete);
        if(badge3)
            badge_3.setBackgroundResource(R.drawable.badge_background_complete);
        if(badge4)
            badge_4.setBackgroundResource(R.drawable.badge_background_complete);
        if(badge5)
            badge_5.setBackgroundResource(R.drawable.badge_background_complete);
        if(badge6)
            badge_6.setBackgroundResource(R.drawable.badge_background_complete);




        //????????? ??????
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SideMenuActivity.class));
            }
        });

        //????????? ?????? ??????
        profile = (TextView) view.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment profilefragment = new ProfileFragment();
                profilefragment.show(getChildFragmentManager(), "profilefragment");
            }
        });

        //?????? ?????? ?????????
        badge_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(badge1)
                    Toast.makeText(getActivity(),"?????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                else
                    showDialogBadge1();
            }
        });

        badge_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(badge2)
                    Toast.makeText(getActivity(),"?????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                else
                    showDialogBadge2();
            }
        });

        badge_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(badge3)
                    Toast.makeText(getActivity(),"?????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                else
                    showDialogBadge3();
            }
        });

        badge_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(badge4)
                    Toast.makeText(getActivity(),"?????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                else
                    showDialogBadge4();
            }
        });

        badge_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(badge5)
                    Toast.makeText(getActivity(),"?????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                else
                    showDialogBadge5();
            }
        });

        badge_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(badge6)
                    Toast.makeText(getActivity(),"?????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                else
                    showDialogBadge6();
            }
        });




        return view;

    }

    private void showDialogBadge1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("????????????")
                .setMessage("\n ????????? ??????????????? ???????????? ?????? ????????? ????????????.")
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    private void showDialogBadge2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("????????????")
                .setMessage("\n ????????? ??????????????? ????????? ??????????????????.")
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    private void showDialogBadge3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("??? ??????")
                .setMessage("\n ????????? ??????????????? ???????????? ???????????? ??????????????????.")
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    private void showDialogBadge4() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("?????????")
                .setMessage("\n ????????? ??????????????? ???????????? 30??? ?????? ??????????????????.")
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    private void showDialogBadge5() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("????????????")
                .setMessage("\n ????????? ??????????????? ???????????? ?????? 20??? ?????? ??????????????????.")
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    private void showDialogBadge6() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("????????????")
                .setMessage("\n ????????? ??????????????? ???????????? ????????? ?????? ??????????????????.")
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }
}
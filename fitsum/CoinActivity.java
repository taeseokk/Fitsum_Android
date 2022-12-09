package org.fitsum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.API.BadgeAPI;
import org.fitsum.API.ChangeAPI;
import org.fitsum.API.ProfileAPI;
import org.fitsum.Dto.BadgeDto;
import org.fitsum.Dto.CommonResult;
import org.fitsum.Dto.SingleResult;
import org.fitsum.Dto.UserDto;
import org.fitsum.FirstPart.FirstActivity;
import org.fitsum.RetrofitAPI.config.RetrofitBuilder;
import org.fitsum.config.AccessTokenSharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinActivity extends AppCompatActivity {

    private Button back;
    private ImageButton ib_basic, ib_exercise, ib_training;
    private String item_s, coin_s;
    private Integer item, coin;


    ProfileAPI profileAPI = RetrofitBuilder.getRetrofit().create(ProfileAPI.class);
    ChangeAPI changeAPI = RetrofitBuilder.getRetrofit().create(ChangeAPI.class);
    BadgeAPI badgeAPI = RetrofitBuilder.getRetrofit().create(BadgeAPI.class);

    UserDto.ChangeUserItemDto changeUserItemDto = new UserDto.ChangeUserItemDto();      //유저 현재상태 옷 변경할때 필요
    UserDto.ChangeUserCoinDto changeUserCoinDto = new UserDto.ChangeUserCoinDto();      //유저 코인 갯수 차감시 필요



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        UserDto.SignupUserDto signupUserDto = new UserDto.SignupUserDto();

        ib_basic = (ImageButton) findViewById(R.id.ib_basic);
        ib_exercise = (ImageButton) findViewById(R.id.ib_exercise);
        ib_training= (ImageButton) findViewById(R.id.ib_training);

        //coin 에는 현재 유저의 코인갯수가 담김
        profileAPI.getUser_coin().enqueue(new Callback<SingleResult>() {
            @Override
            public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                if(response.isSuccessful()){
                    SingleResult data = response.body();
                    coin_s = data.getData().toString();             //coin_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                    coin = (int)Double.parseDouble(coin_s);         //coin 은 그것을 받아서 정수형으로 바꿔주는 역할.

                    Log.d("coin", String.valueOf(coin));


                }
            }

            @Override
            public void onFailure(Call<SingleResult> call, Throwable t) {

            }
        });

        //기본 옷을 눌렀을때.
        ib_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean newBadge2 = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(CoinActivity.this);
                builder.setMessage("옷을 바꾸시겠습니까?")
                        .setTitle("코인 30개 소모")
                        .setPositiveButton("바꾸기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //코인이 30개가 넘을때 수행해야 함. -가 되지 않기위해
                                if(coin >= 30) {

                                    //유저의 현재 옷상태를 가져와서 비교해야함.
                                    profileAPI.getUser_item().enqueue(new Callback<SingleResult>() {
                                        @Override
                                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                            if (response.isSuccessful()) {
                                                SingleResult data = response.body();
                                                item_s = data.getData().toString();             //item_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                                                item = (int) Double.parseDouble(item_s);         //item 은 그것을 받아서 정수형으로 바꿔주는 역할. (실직적 item 숫자가 들어있음)

                                                Log.d("item", String.valueOf(item));
                                                //현재 캐릭터의 옷상태가 기본 옷이라면
                                                if (item == 1 || item == 2 || item == 3 || item == 10 || item == 11 || item == 12) {
                                                    Toast.makeText(CoinActivity.this, "이미 입고있는 옷 입니다.", Toast.LENGTH_SHORT).show();
                                                }

                                                //남자인데 다른옷을 입고있다면.
                                                else if (item == 4 || item == 5 || item == 6 || item == 7 || item == 8 || item == 9) {
                                                    //남자 옷 기본 옷으로 바꾸기
                                                    changeUserItemDto.setCurUserItem(item);
                                                    changeUserItemDto.setNewUserItem(1);
                                                    //유저 옷상태를 변경하기 위한 API
                                                    changeAPI.updateUserItem(changeUserItemDto).enqueue(new Callback<SingleResult>() {
                                                        @Override
                                                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                            if (response.isSuccessful()) {
                                                                changeUserCoinDto.setCurUserCoin(coin);
                                                                changeUserCoinDto.setNewUserCoin(coin - 30);
                                                                //유저의 코인갯수 차감을 위한 API
                                                                changeAPI.updateUserCoin(changeUserCoinDto).enqueue(new Callback<SingleResult>() {
                                                                    @Override
                                                                    public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<SingleResult> call, Throwable t) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<SingleResult> call, Throwable t) {

                                                        }
                                                    });
                                                    Toast.makeText(CoinActivity.this, "옷을 바꿨습니다.", Toast.LENGTH_SHORT).show();
                                                } else if (item == 13 || item == 14 || item == 15 || item == 16 || item == 17 || item == 18) {
                                                    //여자옷 기본 옷으로 바꾸기
                                                    changeUserItemDto.setCurUserItem(item);
                                                    changeUserItemDto.setNewUserItem(10);
                                                    changeAPI.updateUserItem(changeUserItemDto).enqueue(new Callback<SingleResult>() {
                                                        @Override
                                                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                            if (response.isSuccessful()) {
                                                                changeUserCoinDto.setCurUserCoin(coin);
                                                                changeUserCoinDto.setNewUserCoin(coin - 30);
                                                                //유저의 코인갯수 차감을 위한 API
                                                                changeAPI.updateUserCoin(changeUserCoinDto).enqueue(new Callback<SingleResult>() {
                                                                    @Override
                                                                    public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<SingleResult> call, Throwable t) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<SingleResult> call, Throwable t) {

                                                        }
                                                    });
                                                    Toast.makeText(CoinActivity.this, "옷을 바꿨습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        //실패시
                                        @Override
                                        public void onFailure(Call<SingleResult> call, Throwable t) {

                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(CoinActivity.this,"코인이 부족합니다.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

            }
        });

        //운동복을 눌렀을때
        ib_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CoinActivity.this);
                builder.setMessage("옷을 바꾸시겠습니까?")
                        .setTitle("코인 30개 소모")
                        .setPositiveButton("바꾸기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(coin >= 30) {
//유저의 현재 옷상태를 가져와서 비교해야함.
                                    profileAPI.getUser_item().enqueue(new Callback<SingleResult>() {
                                        @Override
                                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                            if (response.isSuccessful()) {
                                                SingleResult data = response.body();
                                                item_s = data.getData().toString();             //item_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                                                item = (int) Double.parseDouble(item_s);         //item 은 그것을 받아서 정수형으로 바꿔주는 역할. (실직적 item 숫자가 들어있음)

                                                Log.d("item", String.valueOf(item));
                                                //현재 캐릭터의 옷상태가 운동복 이라면
                                                if (item == 4 || item == 5 || item == 6 || item == 13 || item == 14 || item == 15) {
                                                    Toast.makeText(CoinActivity.this, "이미 입고있는 옷 입니다.", Toast.LENGTH_SHORT).show();
                                                }
                                                //남자인데 다른옷을 입고있다면.
                                                else if (item == 1 || item == 2 || item == 3 || item == 7 || item == 8 || item == 9) {
                                                    //남자 옷 기본 옷으로 바꾸기
                                                    changeUserItemDto.setCurUserItem(item);
                                                    changeUserItemDto.setNewUserItem(4);

                                                    changeAPI.updateUserItem(changeUserItemDto).enqueue(new Callback<SingleResult>() {
                                                        @Override
                                                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                            if (response.isSuccessful()) {
                                                                changeUserCoinDto.setCurUserCoin(coin);
                                                                changeUserCoinDto.setNewUserCoin(coin - 30);
                                                                //유저의 코인갯수 차감을 위한 API
                                                                changeAPI.updateUserCoin(changeUserCoinDto).enqueue(new Callback<SingleResult>() {
                                                                    @Override
                                                                    public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                                        //첫 구매 성공 뱃지를 활성화 시키기 위한 부분
                                                                        if(response.isSuccessful()){
                                                                            BadgeDto.ChangeBadge3 changeBadge3 = new BadgeDto.ChangeBadge3();
                                                                            changeBadge3.setNewBadge3(true);

                                                                            badgeAPI.badge3(changeBadge3).enqueue(new Callback<CommonResult>() {
                                                                                @Override
                                                                                public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {

                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<CommonResult> call, Throwable t) {

                                                                                }
                                                                            });
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<SingleResult> call, Throwable t) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<SingleResult> call, Throwable t) {

                                                        }
                                                    });
                                                    Toast.makeText(CoinActivity.this, "옷을 바꿨습니다.", Toast.LENGTH_SHORT).show();
                                                } else if (item == 10 || item == 11 || item == 12 || item == 16 || item == 17 || item == 18) {
                                                    //여자옷 기본 옷으로 바꾸기
                                                    changeUserItemDto.setCurUserItem(item);
                                                    changeUserItemDto.setNewUserItem(13);
                                                    changeAPI.updateUserItem(changeUserItemDto).enqueue(new Callback<SingleResult>() {
                                                        @Override
                                                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                            if (response.isSuccessful()) {
                                                                changeUserCoinDto.setCurUserCoin(coin);
                                                                changeUserCoinDto.setNewUserCoin(coin - 30);
                                                                //유저의 코인갯수 차감을 위한 API
                                                                changeAPI.updateUserCoin(changeUserCoinDto).enqueue(new Callback<SingleResult>() {
                                                                    @Override
                                                                    public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                                        //첫 구매 성공 뱃지를 활성화 시키기 위한 부분
                                                                        if(response.isSuccessful()){
                                                                            BadgeDto.ChangeBadge3 changeBadge3 = new BadgeDto.ChangeBadge3();
                                                                            changeBadge3.setNewBadge3(true);

                                                                            badgeAPI.badge3(changeBadge3).enqueue(new Callback<CommonResult>() {
                                                                                @Override
                                                                                public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {

                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<CommonResult> call, Throwable t) {

                                                                                }
                                                                            });
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<SingleResult> call, Throwable t) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<SingleResult> call, Throwable t) {

                                                        }
                                                    });
                                                    Toast.makeText(CoinActivity.this, "옷을 바꿨습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        //실패시
                                        @Override
                                        public void onFailure(Call<SingleResult> call, Throwable t) {

                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(CoinActivity.this,"코인이 부족합니다.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

            }
        });

        ib_training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CoinActivity.this);
                builder.setMessage("옷을 바꾸시겠습니까?")
                        .setTitle("코인 30개 소모")
                        .setPositiveButton("바꾸기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(coin >= 30) {
                                    //유저의 현재 옷상태를 가져와서 비교해야함.
                                    profileAPI.getUser_item().enqueue(new Callback<SingleResult>() {
                                        @Override
                                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                            if (response.isSuccessful()) {
                                                SingleResult data = response.body();
                                                item_s = data.getData().toString();             //item_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                                                item = (int) Double.parseDouble(item_s);         //item 은 그것을 받아서 정수형으로 바꿔주는 역할. (실직적 item 숫자가 들어있음)

                                                Log.d("item", String.valueOf(item));
                                                //현재 캐릭터의 옷상태가 "추리닝" 이라면
                                                if (item == 7 || item == 8 || item == 9 || item == 16 || item == 17 || item == 18) {
                                                    Toast.makeText(CoinActivity.this, "이미 입고있는 옷 입니다.", Toast.LENGTH_SHORT).show();
                                                }
                                                //남자인데 다른옷을 입고있다면.
                                                else if (item == 1 || item == 2 || item == 3 || item == 4 || item == 5 || item == 6) {
                                                    //남자 옷 기본 옷으로 바꾸기
                                                    changeUserItemDto.setCurUserItem(item);         //현재의 옷 번호
                                                    changeUserItemDto.setNewUserItem(7);            //바꿀 옷 번호

                                                    changeAPI.updateUserItem(changeUserItemDto).enqueue(new Callback<SingleResult>() {
                                                        @Override
                                                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                            if (response.isSuccessful()) {
                                                                changeUserCoinDto.setCurUserCoin(coin);
                                                                changeUserCoinDto.setNewUserCoin(coin - 30);
                                                                //유저의 코인갯수 차감을 위한 API
                                                                changeAPI.updateUserCoin(changeUserCoinDto).enqueue(new Callback<SingleResult>() {
                                                                    @Override
                                                                    public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                                        //첫 구매 성공 뱃지를 활성화 시키기 위한 부분
                                                                        if(response.isSuccessful()){
                                                                            BadgeDto.ChangeBadge3 changeBadge3 = new BadgeDto.ChangeBadge3();
                                                                            changeBadge3.setNewBadge3(true);

                                                                            badgeAPI.badge3(changeBadge3).enqueue(new Callback<CommonResult>() {
                                                                                @Override
                                                                                public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {

                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<CommonResult> call, Throwable t) {

                                                                                }
                                                                            });
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<SingleResult> call, Throwable t) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<SingleResult> call, Throwable t) {

                                                        }
                                                    });
                                                    Toast.makeText(CoinActivity.this, "옷을 바꿨습니다.", Toast.LENGTH_SHORT).show();
                                                } else if (item == 11 || item == 12 || item == 13 || item == 14 || item == 15 || item == 10) {
                                                    //여자옷 기본 옷으로 바꾸기
                                                    changeUserItemDto.setCurUserItem(item);         //현재 옷 번호
                                                    changeUserItemDto.setNewUserItem(16);           //바꿀 옷 번호
                                                    changeAPI.updateUserItem(changeUserItemDto).enqueue(new Callback<SingleResult>() {
                                                        @Override
                                                        public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                            if (response.isSuccessful()) {
                                                                changeUserCoinDto.setCurUserCoin(coin);
                                                                changeUserCoinDto.setNewUserCoin(coin - 30);
                                                                //유저의 코인갯수 차감을 위한 API
                                                                changeAPI.updateUserCoin(changeUserCoinDto).enqueue(new Callback<SingleResult>() {
                                                                    @Override
                                                                    public void onResponse(Call<SingleResult> call, Response<SingleResult> response) {
                                                                        //첫 구매 성공 뱃지를 활성화 시키기 위한 부분
                                                                        if(response.isSuccessful()){
                                                                            BadgeDto.ChangeBadge3 changeBadge3 = new BadgeDto.ChangeBadge3();
                                                                            changeBadge3.setNewBadge3(true);

                                                                            badgeAPI.badge3(changeBadge3).enqueue(new Callback<CommonResult>() {
                                                                                @Override
                                                                                public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {

                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<CommonResult> call, Throwable t) {

                                                                                }
                                                                            });
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<SingleResult> call, Throwable t) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<SingleResult> call, Throwable t) {

                                                        }
                                                    });
                                                    Toast.makeText(CoinActivity.this, "옷을 바꿨습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        //실패시
                                        @Override
                                        public void onFailure(Call<SingleResult> call, Throwable t) {

                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(CoinActivity.this,"코인이 부족합니다.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

            }
        });



        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(CoinActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}
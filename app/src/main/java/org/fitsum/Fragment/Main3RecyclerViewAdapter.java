package org.fitsum.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miguelrochefort.fitnesscamera.R;

import org.fitsum.Dto.BoardDto;

import java.util.List;

public class Main3RecyclerViewAdapter extends RecyclerView.Adapter<Main3RecyclerViewAdapter.CustomViewHolder> {

    private Context context;
    private List<BoardDto.BoardViewDto> items;


    //리사이클러 뷰 개개인의 (카드뷰) 선언
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView content, title, name,writeDate;
        CheckBox liked;


        public CustomViewHolder(@NonNull View itemView) {

            super(itemView);

            title = itemView.findViewById(R.id.title_tv);                   //게시판 제목
            content = itemView.findViewById(R.id.content_tv);               //게시판 내용
            name = itemView.findViewById(R.id.name_tv);                     //게시판 이름
            writeDate = itemView.findViewById(R.id.writeDate);              //게시판 작성날짜
//            liked = itemView.findViewById(R.id.liked);                      //게시글 좋아요

        }
    }

    public Main3RecyclerViewAdapter(Context context, List<BoardDto.BoardViewDto> items) {
        this.context = context;
        this.items = items;                 //BoardViewDto 로 넘어온 데이터들
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main3_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    //게시판 정보들 불러와서 클릭??
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        BoardDto.BoardViewDto item = items.get(position);
        //이름, 제목, 내용 받아오기
        holder.name.setText(item.getNickName());
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.writeDate.setText(item.getWriteDate().toString());


//        try{
//            if(item.getLiked()){
//                holder.liked.setChecked(true);
//            }
//            else
//                holder.liked.setChecked(false);
//        }
//        catch (NullPointerException e){
//            e.printStackTrace();
//        }

//        holder.liked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BoardListAPI boardListAPI = RetrofitBuilder.getRetrofit().create(BoardListAPI.class);
//
//                boardListAPI.updateLike(item.getLiked(), item.getboardId());
//
//            }
//        });

    }


    //아이템 가져오기 (갯수만큼)
    @Override
    public int getItemCount() {
        try {
            return this.items.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }
}

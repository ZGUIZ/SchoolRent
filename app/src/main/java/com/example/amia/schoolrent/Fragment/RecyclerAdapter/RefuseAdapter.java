package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class RefuseAdapter extends RecyclerView.Adapter<RefuseAdapter.Holder> {

    private List<ResponseInfo> responseInfos;
    private Context context;

    private OnItemClickListener onItemClickListener;
    private SecondRefuseListener onClickListener;
    private IconClickListener iconClickListener;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public RefuseAdapter(Context context,List<ResponseInfo> responseInfos) {
        this.responseInfos = responseInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(context).inflate(R.layout.refuse_item_layout,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        ResponseInfo responseInfo = responseInfos.get(position);
        final Student student = responseInfo.getStudent();
        Glide.with(context).load(student.getUserIcon()).into(holder.userIcon);
        holder.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconClickListener.onClick(student);
            }
        });

        holder.userName.setText(student.getUserName());
        holder.message.setText(responseInfo.getResponseInfo());
        holder.createDate.setText(sdf.format(responseInfo.getResponseDate()));

        List<SecondResponseInfo> secondResponseInfos = responseInfo.getSecondResponseInfos();
        if(secondResponseInfos!=null &&secondResponseInfos.size()>0){
            //清空所有组件
            holder.linearLayout.removeAllViews();
            holder.linearLayout.setVisibility(View.VISIBLE);
            for(final SecondResponseInfo secondResponseInfo :secondResponseInfos){
                final Student s = secondResponseInfo.getStudent();
                if(s == null){
                    continue;
                }
                View view = LayoutInflater.from(context).inflate(R.layout.second_refuse_item_layout,holder.linearLayout,false);
                TextView message = view.findViewById(R.id.sec_refuse_message);
                String responseMessage = secondResponseInfo.getResponseInfo();
                if(secondResponseInfo.getAlterUser()!=null){

                }
                message.setText(responseMessage);
                TextView date =view.findViewById(R.id.create_time);
                date.setText(sdf.format(secondResponseInfo.getResponseDate()));

                ImageView icon = view.findViewById(R.id.sec_user_icon);

                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iconClickListener.onClick(s);
                    }
                });

                Glide.with(context).load(s.getUserIcon()).into(icon);
                TextView userName = view.findViewById(R.id.second_user_name);
                userName.setText(s.getUserName());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickListener.onClick(secondResponseInfo);
                    }
                });
                holder.linearLayout.addView(view);
            }
        } else {
            holder.linearLayout.setVisibility(View.GONE);
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemCLick(holder.itemLayout,position);
            }
        });
        holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return responseInfos.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        ImageView userIcon;
        TextView userName;
        TextView message;
        TextView createDate;
        LinearLayout linearLayout;
        RelativeLayout itemLayout;


        public Holder(View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.user_icon);
            userName = itemView.findViewById(R.id.user_name);
            message = itemView.findViewById(R.id.refuse_message);
            createDate = itemView.findViewById(R.id.create_time);
            linearLayout = itemView.findViewById(R.id.second_refuse_layout);
            itemLayout = itemView.findViewById(R.id.refuse_item_rl);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setSecondClickListener(SecondRefuseListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public void setIconClickListener(IconClickListener iconClickListener) {
        this.iconClickListener = iconClickListener;
    }
}

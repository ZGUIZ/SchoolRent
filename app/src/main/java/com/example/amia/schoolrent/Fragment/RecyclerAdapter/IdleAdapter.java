package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;

import java.util.ArrayList;
import java.util.List;

public class IdleAdapter extends RecyclerView.Adapter<IdleAdapter.Holder> {

    private List<IdleInfo> idleInfos;
    private Context context;

    private OnItemClickListener onItemClickListener;

    public IdleAdapter(Context context) {
        this.idleInfos = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Holder holder = new Holder(LayoutInflater.from(context).inflate(R.layout.idle_item_layout,parent,false));

        //添加点击事件
        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemCLick(holder.infoLayout,holder.getLayoutPosition());
            }
        });

        holder.infoLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemLongClick(holder.infoLayout,pos);
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        IdleInfo idleInfo = idleInfos.get(position);
        Student student = idleInfo.getStudent();
        holder.title.setText(idleInfo.getTitle());
        holder.user_name.setText(student.getUserName());
        holder.credit.setText(String.valueOf(student.getCredit()));


        String userIconUrl = student.getUserIcon();
        if(userIconUrl == null || "".equals(userIconUrl.trim())){
            holder.user_icon.setImageResource(R.drawable.default_icon);
        } else {
            Glide.with(context).load(student.getUserIcon()).into(holder.user_icon);
        }
        //加载闲置图片
        List<IdelPic> idelPicList = idleInfo.getPicList();
        Glide.with(context).load(idelPicList.get(0).getPicUrl()).into(holder.idle_icon);
    }

    @Override
    public int getItemCount() {
        return idleInfos.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView idle_icon;
        TextView title;

        ImageView user_icon;
        TextView user_name;
        TextView credit;

        LinearLayout infoLayout;
        public Holder(View itemView) {
            super(itemView);
            idle_icon = itemView.findViewById(R.id.info_pic_iv);
            title = itemView.findViewById(R.id.title_tv);
            user_icon = itemView.findViewById(R.id.user_icon);
            user_name = itemView.findViewById(R.id.user_name_tv);
            credit = itemView.findViewById(R.id.credit_tv);

            infoLayout = itemView.findViewById(R.id.idle_info_item_ll);
        }
    }

    public List<IdleInfo> getIdleInfos() {
        return idleInfos;
    }

    public void setIdleInfos(List<IdleInfo> idleInfos) {
        this.idleInfos = idleInfos;
        this.notifyDataSetChanged();
    }

    public void addIdleInfos(List<IdleInfo> idleInfos){
        for(int i = 0 ;i<idleInfos.size();i++){
            this.idleInfos.add(idleInfos.get(i));
        }
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public IdleInfo getIdleInfoByPosition(int poition){
        return idleInfos.get(poition);
    }
}

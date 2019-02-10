package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdleAdapter extends RecyclerView.Adapter<IdleAdapter.Holder> {

    private List<IdleInfo> idleInfos;
    private Map<String, Bitmap> iconMap;
    private Map<String,String> needMap;
    private Context context;
    private LoadIconInterface loadIconInterface;

    public IdleAdapter(LoadIconInterface loadIconInterface, Context context) {
        this.idleInfos = new ArrayList<>();
        this.context = context;
        this.iconMap = new HashMap<>();
        this.needMap = new HashMap<>();
        this.loadIconInterface = loadIconInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(context).inflate(R.layout.idle_item_layout,parent,false));
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
        } else if(iconMap.get(idleInfo.getUserId())!=null){
            holder.user_icon.setImageBitmap(iconMap.get(idleInfo.getUserId()));
        }
        //加载用户头像
        if(iconMap.containsKey(student.getUserIcon())){
            holder.user_icon.setImageBitmap(iconMap.get(student.getUserIcon()));
        } else {
            if(student.getUserIcon()!=null && "".equals(student.getUserIcon()) && !needMap.containsKey(student.getUserId())){
                needMap.put(student.getUserId(),student.getUserIcon());
                loadIconInterface.loadBitmap(student.getUserIcon(),student.getUserId(),needMap);
            }
            holder.idle_icon.setImageResource(R.drawable.default_icon);
        }
        //加载闲置图片
        if(iconMap.containsKey(idleInfo.getInfoId())){
            holder.idle_icon.setImageBitmap(iconMap.get(idleInfo.getInfoId()));
        } else {
            List<IdelPic> idelPicList = idleInfo.getPicList();
            if (idelPicList.size() > 0 && !needMap.containsKey(idleInfo.getInfoId())) {
                needMap.put(idleInfo.getInfoId(),idelPicList.get(0).getPicUrl());
                loadIconInterface.loadBitmap(idelPicList.get(0).getPicUrl(),idleInfo.getInfoId(),needMap);
            }
            holder.idle_icon.setImageResource(R.drawable.default_icon);
        }
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

        public Holder(View itemView) {
            super(itemView);

            idle_icon = itemView.findViewById(R.id.info_pic_iv);
            title = itemView.findViewById(R.id.title_tv);
            user_icon = itemView.findViewById(R.id.user_icon);
            user_name = itemView.findViewById(R.id.user_name_tv);
            credit = itemView.findViewById(R.id.credit_tv);
        }
    }

    public interface LoadIconInterface{
        void loadBitmap(String url, String id, Map<String,String> needMap);
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

    public void clearImage(){
        for(String id:iconMap.keySet()){
            iconMap.get(id).recycle();
        }
        iconMap.clear();
    }

    public void addImage(String id,Bitmap bitmap){
        needMap.remove(id);
        iconMap.put(id,bitmap);
        this.notifyDataSetChanged();
    }
}

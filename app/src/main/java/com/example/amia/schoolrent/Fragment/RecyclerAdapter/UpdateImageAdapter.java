package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.LocalPic;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.BitMapUtil;

import java.util.ArrayList;
import java.util.List;

public class UpdateImageAdapter extends RecyclerView.Adapter<UpdateImageAdapter.Holder> {
    private List<IdelPic> picList;
    private List<IdelPic> realList;

    private Context context;

    private OnItemClickListener mOnItemClickListener;

    private String addButtonId = "00000000";

    public UpdateImageAdapter(Context context) {
        this.context = context;
        this.picList = new ArrayList<>();
        this.realList = new ArrayList<>();
        IdelPic pic = new IdelPic();
        pic.setPicId(addButtonId);
        picList.add(pic);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(context).inflate(R.layout.pic_item_layout,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        IdelPic pic = picList.get(position);
        if(addButtonId.equals(pic.getPicId())){
            holder.addRl.setVisibility(View.VISIBLE);
            holder.idleRl.setVisibility(View.GONE);
            holder.addRl.setOnClickListener(onClickListener);
        } else {
            holder.addRl.setVisibility(View.GONE);
            holder.idleRl.setVisibility(View.VISIBLE);

            Glide.with(context).load(pic.getPicUrl()).into(holder.idleImage);

            holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removePic(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return picList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView delBtn;
        ImageView idleImage;

        RelativeLayout idleRl;
        RelativeLayout addRl;

        public Holder(View itemView) {
            super(itemView);
            idleImage = itemView.findViewById(R.id.idle_iv);
            delBtn = itemView.findViewById(R.id.close_btn);
            idleRl = itemView.findViewById(R.id.idle_rl);
            addRl = itemView.findViewById(R.id.add_rl);
        }
    }

    public void addPic(IdelPic localPic) {
        localPic.setBeanStatus("add");
        picList.add(picList.size()-1,localPic);
        realList.add(localPic);
        this.notifyDataSetChanged();
    }

    public void removePic(int position){
        IdelPic idelPic = picList.get(position);
        if(!"add".equals(idelPic.getBeanStatus())){
            idelPic.setBeanStatus("del");
        } else {
            //找到图片并移除
            for(int i = 0;i<realList.size();i++){
                IdelPic pic = realList.get(i);
                if(pic ==idelPic){
                    realList.remove(i);
                    break;
                }
            }
        }
        picList.remove(position);

        this.notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void addPic();
    }

    public void setOnItemCLickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.add_rl:
                    if(mOnItemClickListener!=null) {
                        mOnItemClickListener.addPic();
                    }
                    break;
            }
        }
    };

    public List<IdelPic> getPicList(){
        return realList;
    }

    public void setPicList(List<IdelPic> idelPics){
        picList.clear();
        realList.clear();

        picList.addAll(idelPics);
        realList.addAll(idelPics);

        IdelPic pic = new IdelPic();
        pic.setPicId(addButtonId);
        picList.add(pic);

        notifyDataSetChanged();
    }
}

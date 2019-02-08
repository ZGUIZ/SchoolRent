package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.LocalPic;
import com.example.amia.schoolrent.R;

import java.util.ArrayList;
import java.util.List;

public class PushImageAdapter extends RecyclerView.Adapter<PushImageAdapter.Holder> {
    private List<LocalPic> picList;
    private Context context;

    private OnItemClickListener mOnItemClickListener;

    public PushImageAdapter(Context context) {
        this.context = context;
        this.picList = new ArrayList<>();
        LocalPic pic = new LocalPic();
        pic.setAdd(true);
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
        LocalPic pic = picList.get(position);
        if(pic.isAdd()){
            holder.addRl.setVisibility(View.VISIBLE);
            holder.idleRl.setVisibility(View.GONE);
            holder.addRl.setOnClickListener(onClickListener);
        } else {
            holder.addRl.setVisibility(View.GONE);
            holder.idleRl.setVisibility(View.VISIBLE);
            holder.idleImage.setImageBitmap(BitmapFactory.decodeFile(pic.getLocalUri()));

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

    public void addPic(LocalPic localPic) {
        picList.add(localPic);
        this.notifyDataSetChanged();
    }

    public void removePic(int position){
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
}

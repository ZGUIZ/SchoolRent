package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.R;

import java.util.List;
import java.util.Map;

public class IndexClassifyAdapter extends RecyclerView.Adapter<IndexClassifyAdapter.Holder> {
    private List<Classify> list;
    //private Map<String,Bitmap> iconMap;
    private Context context;
    private OnClickListener onClickListener;

    public IndexClassifyAdapter(List<Classify> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(context).inflate(R.layout.bottom_button_layout,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final Classify classify = list.get(position);
        holder.textView.setText(classify.getClassifyName());
        if(classify.getImageUrl() == null || "".equals(classify.getImageUrl().trim())){
            holder.imageView.setImageResource(R.drawable.default_icon);
        } else {
            Glide.with(context).load(classify.getImageUrl()).into(holder.imageView);
        }

        holder.btnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener!=null){
                    onClickListener.onCLick(classify.getClassifyId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        RelativeLayout btnLayout;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
            btnLayout = itemView.findViewById(R.id.btn_rl);
        }
    }

    public interface OnClickListener{
        void onCLick(String id);
    }
}

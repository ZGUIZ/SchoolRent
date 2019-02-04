package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.R;

import java.util.List;

public class IndexClassifyAdapter extends RecyclerView.Adapter<IndexClassifyAdapter.Holder> {
    private List<Classify> list;
    private Context context;

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
        Classify classify = list.get(position);
        holder.textView.setText(classify.getClassifyName());
        if(classify.getImageUrl() == null || "".equals(classify.getImageUrl().trim())){
            holder.imageView.setImageResource(R.drawable.default_icon);
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}

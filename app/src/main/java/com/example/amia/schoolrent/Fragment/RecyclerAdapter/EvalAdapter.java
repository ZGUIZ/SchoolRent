package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.Eval;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class EvalAdapter extends RecyclerView.Adapter<EvalAdapter.Holder> {

    private List<Eval> evals;
    private Context context;

    public EvalAdapter(Context context) {
        this.evals = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Holder holder = new Holder(LayoutInflater.from(context).inflate(R.layout.eval_item_layout,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Eval eval = evals.get(position);
        Student student = eval.getStudent();
        holder.ratingBar.setRating((float) eval.getLevel());
        Glide.with(context).load(student.getUserIcon()).into(holder.userIcon);
        holder.userName.setText(student.getUserName());
        holder.createDate.setText(DateUtil.formatDate(eval.getEvalDate(),"yyyy-MM-dd HH:mm:ss"));
        holder.eval.setText(eval.getContent());
    }

    @Override
    public int getItemCount() {
        return evals.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView userIcon;
        TextView userName;
        TextView eval;
        TextView createDate;
        RatingBar ratingBar;

        public Holder(View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.user_icon);
            userName = itemView.findViewById(R.id.user_name);
            eval = itemView.findViewById(R.id.context);
            createDate = itemView.findViewById(R.id.time);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    public void setIdleInfos(List<Eval> evals) {
        this.evals = evals;
        this.notifyDataSetChanged();
    }

    public void addIdleInfos(List<Eval> evals){
        this.evals.addAll(evals);
        this.notifyDataSetChanged();
    }
}

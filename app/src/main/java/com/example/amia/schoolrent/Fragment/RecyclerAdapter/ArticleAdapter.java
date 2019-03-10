package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.View.RoundImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.Holder> {

    private List<RentNeeds> rentNeeds;
    private Context context;

    private SimpleDateFormat sdf;

    private OnItemClickListener onItemClickListener;

    public ArticleAdapter(Context context) {
        this.rentNeeds = new ArrayList<>();
        this.context = context;
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Holder holder = new Holder(LayoutInflater.from(context).inflate(R.layout.needs_item,parent,false));

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
        RentNeeds needs = rentNeeds.get(position);
        holder.title.setText(needs.getTitle());
        holder.info.setText(needs.getIdelInfo());
        Date createDate = needs.getCreateDate();
        holder.createDate.setText(sdf.format(createDate));

        Student student = needs.getStudent();
        holder.userName.setText(student.getUserName());
        Glide.with(context).load(student.getUserIcon()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return rentNeeds.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        RoundImageView imageView;
        TextView userName;
        TextView info;
        TextView title;
        TextView createDate;

        LinearLayout infoLayout;

        public Holder(View itemView) {
            super(itemView);

            infoLayout = itemView.findViewById(R.id.info_layout);
            imageView = itemView.findViewById(R.id.user_icon);
            userName = itemView.findViewById(R.id.user_name);
            info = itemView.findViewById(R.id.context);
            title = itemView.findViewById(R.id.title);
            createDate = itemView.findViewById(R.id.create_date);
        }
    }

    public void setNeedsInfos(List<RentNeeds> rentNeeds) {
        this.rentNeeds = rentNeeds;
        this.notifyDataSetChanged();
    }

    public void addIdleInfos(List<RentNeeds> rentNeeds){
        this.rentNeeds.addAll(rentNeeds);
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public RentNeeds getIdleInfoByPosition(int poition){
        return rentNeeds.get(poition);
    }
}

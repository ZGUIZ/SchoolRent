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

import com.example.amia.schoolrent.Bean.CheckStatement;
import com.example.amia.schoolrent.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CheckStateAdapter extends RecyclerView.Adapter<CheckStateAdapter.Holder> {

    private List<CheckStatement> datas;

    private Context context;

    private SimpleDateFormat sdf;

    public CheckStateAdapter(Context context) {
        this.datas = new ArrayList<>();
        this.context = context;
        sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(context).inflate(R.layout.statement_item_layout,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CheckStatement statement = datas.get(position);
        holder.msg.setText(statement.getMemo());
        holder.amount.setText(String.valueOf(statement.getAmount()));
        holder.createDate.setText(sdf.format(statement.getCreateDate()));
        holder.symbol.setText(statement.getType() == 0 ? "+":"-");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView msg;
        TextView createDate;
        TextView amount;
        TextView symbol;

        public Holder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.type_msg);
            createDate = itemView.findViewById(R.id.create_date);
            amount = itemView.findViewById(R.id.amount);
            symbol = itemView.findViewById(R.id.symbol);
        }
    }

    public void setDatas(List<CheckStatement> datas){
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addDatas(List<CheckStatement> datas){
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }
}

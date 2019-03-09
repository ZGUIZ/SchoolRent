package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

public class FinishedAdapter extends RecyclerView.Adapter<FinishedAdapter.RecViewholder> {
    private Context context;
    private List<Rent> data = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private ResponseRentInterface responseRentInterface;

    public FinishedAdapter(Context context, ResponseRentInterface responseRentInterface) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

        this.responseRentInterface = responseRentInterface;
    }

    public void setList(List<Rent> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void addRent(List<Rent> rentList){
        data.addAll(rentList);
        notifyDataSetChanged();
    }

    @Override
    public RecViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.finished_item, parent, false);
        return new RecViewholder(view);
    }

    @Override
    public void onBindViewHolder(RecViewholder holder, final int position) {
        final Rent rent = data.get(position);
        IdleInfo idleInfo = rent.getIdleInfo();
        holder.title.setText(idleInfo.getTitle());
        holder.money.setText(String.valueOf(idleInfo.getRetal()));
        holder.deposit.setText(String.valueOf(idleInfo.getDeposit()));

        List<IdelPic> idelPicList = idleInfo.getPicList();
        IdelPic pic = idelPicList.get(0);

        Glide.with(context).load(pic.getPicUrl()).into(holder.idleIcon);
        //状态
        String status = null;
        switch (idleInfo.getStatus()){
            case 0:
                status = ActivityUtil.getString(context,R.string.never_rent);
                break;
            case 1:
                status = ActivityUtil.getString(context,R.string.confirm_rent);
                break;
            case 2:
                status = ActivityUtil.getString(context,R.string.renting);
                break;
            case 3:
                status = ActivityUtil.getString(context,R.string.finished);
                break;
            case 4:
                status = ActivityUtil.getString(context,R.string.close_by_self);
                break;
            case 5:
                status = ActivityUtil.getString(context,R.string.finished);
                break;
            case 6:
                status = ActivityUtil.getString(context,R.string.cancel);
                break;
            case 8:
                status = ActivityUtil.getString(context,R.string.request_return);
                break;
            case 100:
                status = ActivityUtil.getString(context,R.string.admin_close);
                break;
        }
        holder.status.setText(status);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.my_request_item_rl:
                        responseRentInterface.toIdle(rent.getIdleInfo());
                        break;
                    case R.id.delete_btn:
                        responseRentInterface.delete(rent);
                        break;
                }
            }
        };

        holder.deleteBtn.setOnClickListener(onClickListener);
        holder.item.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * view.getWidth()获取的是屏幕中可以看到的大小.
     */
    public class RecViewholder extends RecyclerView.ViewHolder {

        ImageView idleIcon;
        TextView title;
        TextView money;
        TextView status;
        TextView deposit;

        Button deleteBtn;

        RelativeLayout item;

        public RelativeLayout slideItem;

        public RecViewholder(View itemView) {
            super(itemView);
            idleIcon = itemView.findViewById(R.id.idle_icon);
            title = itemView.findViewById(R.id.title_tv);
            money = itemView.findViewById(R.id.money);
            status = itemView.findViewById(R.id.status);
            deposit = itemView.findViewById(R.id.deposit);

            slideItem = itemView.findViewById(R.id.slide_itemView);
            item = itemView.findViewById(R.id.my_request_item_rl);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
        }

    }
    public interface ResponseRentInterface{
        void delete(Rent rent);
        void toIdle(IdleInfo idleInfo);
    }
}

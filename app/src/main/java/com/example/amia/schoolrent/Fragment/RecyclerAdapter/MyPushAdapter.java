package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.Extension;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANG on 18/4/24.
 */

public class MyPushAdapter extends RecyclerView.Adapter<MyPushAdapter.RecViewholder> {

    private Context context;
    private List<IdleInfo> data = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private ResponseRentInterface responseRentInterface;

    public MyPushAdapter(Context context, ResponseRentInterface responseRentInterface) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

        this.responseRentInterface = responseRentInterface;
    }

    public void setList(List<IdleInfo> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void addIdle(List<IdleInfo> list){
        data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.my_push_item, parent, false);
        return new RecViewholder(view);
    }

    @Override
    public void onBindViewHolder(RecViewholder holder, final int position) {
        final IdleInfo idleInfo = data.get(position);
        List<IdelPic> idlePics = idleInfo.getPicList();
        IdelPic idlePic = idlePics.get(0);
        Glide.with(context).load(idlePic.getPicUrl()).into(holder.idleIcon);
        holder.title.setText(idleInfo.getTitle());
        holder.money.setText(String.valueOf(idleInfo.getRetal()));
        holder.deposit.setText(String.valueOf(idleInfo.getDeposit()));

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
            case 100:
                status = ActivityUtil.getString(context,R.string.admin_close);
                break;
        }
        holder.status.setText(status);

        if(idleInfo.getStatus()!=0){
            holder.close.setBackgroundColor(Color.rgb(200,199,205));
        } else {
            holder.close.setBackgroundColor(Color.rgb(254,60,49));
        }

        holder.slideItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseRentInterface.loadDetail(idleInfo);
            }
        });

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //只有未开始租赁的可以下架
                if(idleInfo.getStatus() == 0){
                    responseRentInterface.closeIdle(idleInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * view.getWidth()获取的是屏幕中可以看到的大小.
     */
   public class RecViewholder extends RecyclerView.ViewHolder implements Extension {

        ImageView idleIcon;
        TextView title;
        TextView money;
        TextView status;
        TextView deposit;

        TextView close;

        public LinearLayout slide;
        public RelativeLayout slideItem;

        public RecViewholder(View itemView) {
            super(itemView);
            idleIcon = itemView.findViewById(R.id.idle_icon);
            title = itemView.findViewById(R.id.title_tv);
            money = itemView.findViewById(R.id.money);
            status = itemView.findViewById(R.id.status);
            deposit = itemView.findViewById(R.id.deposit);

            close = itemView.findViewById(R.id.close_tv);
            slideItem = itemView.findViewById(R.id.slide_itemView);
            slide = itemView.findViewById(R.id.slide);
        }

        @Override
        public float getActionWidth() {
            //布局隐藏超过父布局的范围的时候这里得不到宽度
            return  dip2px(context,80);
        }

    }

    /**
     * 根据手机分辨率从DP转成PX
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface ResponseRentInterface{
        void loadDetail(IdleInfo idleInfo);
        void closeIdle(IdleInfo idleInfo);
    }
}

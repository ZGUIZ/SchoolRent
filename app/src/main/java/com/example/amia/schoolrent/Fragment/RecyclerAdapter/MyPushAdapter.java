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

        if(idleInfo.getStatus() == 0 || idleInfo.getStatus() == 1){
            holder.edit.setBackgroundColor(Color.rgb(255,157,0));
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(idleInfo.getStatus()!=0 && idleInfo.getStatus()!=1){
                        return;
                    }
                    responseRentInterface.updateIdle(idleInfo);
                }
            });
        } else if(idleInfo.getStatus() == 5){
            //如果提交损毁信息，则设置为详细按钮
            holder.edit.setBackgroundColor(Color.rgb(255,157,0));
            holder.edit.setText(R.string.detail);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    responseRentInterface.startRent(idleInfo);
                }
            });
        } else if(idleInfo.getStatus() == 2 || idleInfo.getStatus() == 8){
            //如果是正在租赁，或者申请返还，则可以设置赔偿金额
            holder.edit.setBackgroundColor(Color.rgb(255,157,0));
            holder.edit.setText(R.string.complain);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    responseRentInterface.complain(idleInfo);
                }
            });
        } else {
            holder.edit.setBackgroundColor(Color.rgb(200,199,205));
        }

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
                status = ActivityUtil.getString(context,R.string.submit_destroy);
                break;
            case 8:
                status = ActivityUtil.getString(context,R.string.request_return);
                break;
            case 9:
                status = ActivityUtil.getString(context,R.string.had_eval);
                break;
            case 10:
                status = ActivityUtil.getString(context,R.string.submit_destroy);
                break;
            case 100:
                status = ActivityUtil.getString(context,R.string.admin_close);
                break;
        }
        holder.status.setText(status);

        switch (idleInfo.getStatus()){
            case 0:
                holder.close.setBackgroundColor(Color.rgb(254,60,49));
                holder.close.setText(R.string.close_btn);
                break;
            case 1:
                holder.close.setBackgroundColor(Color.rgb(255,60,49));
                holder.close.setText(R.string.cancle_rent);
                break;
            case 2:
            case 8:
                holder.close.setBackgroundColor(Color.rgb(255,60,49));
                holder.close.setText(R.string.finish);
                break;
            case 5:
                holder.close.setBackgroundColor(Color.rgb(255,60,49));
                holder.close.setText(R.string.refuse_rent);
                break;
            default:
                holder.close.setBackgroundColor(Color.rgb(254,60,49));
                holder.close.setText(R.string.delete);
                break;
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
                switch (idleInfo.getStatus()){
                    case 0:
                        //下架
                        responseRentInterface.closeIdle(idleInfo);
                        break;
                    case 1:
                    case 2:
                    case 5:
                    case 8:
                        //取消或完成
                        responseRentInterface.cancleOrFinish(idleInfo);
                        break;
                    default:
                        //删除
                        responseRentInterface.delIdle(idleInfo);
                        break;
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
        TextView edit;

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
            edit = itemView.findViewById(R.id.edit_tv);

            close = itemView.findViewById(R.id.close_tv);
            slideItem = itemView.findViewById(R.id.slide_itemView);
            slide = itemView.findViewById(R.id.slide);
        }

        @Override
        public float getActionWidth() {
            //布局隐藏超过父布局的范围的时候这里得不到宽度
            return  dip2px(context,160);
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
        void cancleOrFinish(IdleInfo idleInfo);
        void delIdle(IdleInfo idleInfo);
        void updateIdle(IdleInfo idleInfo);
        void startRent(IdleInfo idleInfo);
        void complain(IdleInfo idleInfo);
    }
}

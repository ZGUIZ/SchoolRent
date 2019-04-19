package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.Extension;
import com.example.amia.schoolrent.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANG on 18/4/24.
 */

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewholder> {

    private Context context;
    private List<Rent> data = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private ResponseRentInterface responseRentInterface;

    public RecAdapter(Context context,ResponseRentInterface responseRentInterface) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

        this.responseRentInterface = responseRentInterface;
    }

    public void setList(List<Rent> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rent_person_item, parent, false);
        return new RecViewholder(view);
    }

    @Override
    public void onBindViewHolder(RecViewholder holder, final int position) {
        final Rent rent = data.get(position);
        Student student = rent.getStudent();
        Glide.with(context).load(student.getUserIcon()).into(holder.userIcon);
        holder.userName.setText(student.getUserName());
        holder.credit.setText(String.valueOf(student.getCredit()));

        if(rent.getStatus() == 1 || rent.getStatus() == 5 || rent.getStatus() == 4){
            holder.agreeIcon.setImageResource(R.drawable.agree);
        } else if(rent.getStatus() == 2 || rent.getStatus() == 3){
            holder.agreeIcon.setImageResource(R.drawable.not_agree);
        } else {
            holder.agreeIcon.setVisibility(View.GONE);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.agree_tv:
                        responseRentInterface.agree(rent);
                        break;
                    case R.id.refuse_tv:
                        responseRentInterface.refuse(rent);
                        break;
                    case R.id.user_info_rl:
                        responseRentInterface.showUserMsg(rent.getStudent());
                        break;
                }
            }
        };

        //如果已经回复，则不可点击
        if(rent.getStatus() == 0) {
            holder.agree.setOnClickListener(onClickListener);
            holder.refuse.setOnClickListener(onClickListener);
            holder.agree.setBackgroundColor(Color.rgb(255,157,0));
            holder.refuse.setBackgroundColor(Color.rgb(254,60,49));
        } else {
            holder.agree.setBackgroundColor(Color.rgb(200,199,205));
            holder.refuse.setBackgroundColor(Color.rgb(200,199,205));
        }
        holder.infoLayout.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * view.getWidth()获取的是屏幕中可以看到的大小.
     */
   public class RecViewholder extends RecyclerView.ViewHolder implements Extension {

        ImageView userIcon;
        TextView userName;
        TextView credit;
        ImageView agreeIcon;

        TextView agree;
        TextView refuse;

        RelativeLayout infoLayout;

        public RelativeLayout slideItem;

        public RecViewholder(View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.rent_user_icon);
            userName = itemView.findViewById(R.id.user_name_tv);
            credit = itemView.findViewById(R.id.credit_tv);
            agreeIcon = itemView.findViewById(R.id.agree_icon);
            agree = itemView.findViewById(R.id.agree_tv);
            refuse = itemView.findViewById(R.id.refuse_tv);
            slideItem = itemView.findViewById(R.id.slide_itemView);

            infoLayout = itemView.findViewById(R.id.user_info_rl);
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
        void agree(Rent rent);
        void refuse(Rent rent);
        void showUserMsg(Student student);
    }
}

package com.example.amia.schoolrent.Fragment.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Bean.Message;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.Extension;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANG on 18/4/24.
 */

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.RecViewholder> {

    private Context context;
    private List<Message> data = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private OnClickListener onClick;

    public MyMessageAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<Message> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.my_message_item, parent, false);
        return new RecViewholder(view);
    }

    @Override
    public void onBindViewHolder(RecViewholder holder, final int position) {
        final Message message = data.get(position);
        holder.title.setText(message.getTitle());
        holder.desc.setText(message.getContent());
        holder.createDate.setText(DateUtil.formatDate(message.getCreateDate(),"MM-dd hh:mm"));

        holder.slideItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onCLick(message);
            }
        });

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onDel(message);
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

        TextView title;
        TextView desc;
        TextView createDate;

        TextView delBtn;

        public RelativeLayout slideItem;

        public RecViewholder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.content);
            createDate = itemView.findViewById(R.id.date);

            delBtn = itemView.findViewById(R.id.del_btn);

            slideItem = itemView.findViewById(R.id.slide_itemView);
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

    public OnClickListener getOnClick() {
        return onClick;
    }

    public void setOnClick(OnClickListener onClick) {
        this.onClick = onClick;
    }

    public interface OnClickListener{
        void onCLick(Message msg);
        void onDel(Message msg);
    }
}

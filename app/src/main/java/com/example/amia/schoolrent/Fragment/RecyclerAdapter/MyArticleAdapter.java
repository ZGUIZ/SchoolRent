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
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.Extension;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.RentNeedsTask;
import com.example.amia.schoolrent.Util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANG on 18/4/24.
 */

public class MyArticleAdapter extends RecyclerView.Adapter<MyArticleAdapter.RecViewholder> {

    private Context context;
    private List<RentNeeds> data = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private ResponseRentInterface responseRentInterface;

    public MyArticleAdapter(Context context, ResponseRentInterface responseRentInterface) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

        this.responseRentInterface = responseRentInterface;
    }

    public void setList(List<RentNeeds> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<RentNeeds> list){
        data.addAll(list);
    }

    @Override
    public RecViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.my_article_item, parent, false);
        return new RecViewholder(view);
    }

    @Override
    public void onBindViewHolder(RecViewholder holder, final int position) {
        final RentNeeds rentNeeds = data.get(position);
        holder.title.setText(rentNeeds.getTitle());
        holder.artile.setText(rentNeeds.getIdelInfo());
        holder.createDate.setText(DateUtil.formatDate(rentNeeds.getCreateDate(),"yyyy-MM-dd"));

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.close_tv:
                        responseRentInterface.del(rentNeeds);
                        break;
                    case R.id.slide_itemView:
                        responseRentInterface.toInfo(rentNeeds);
                        break;
                }
            }
        };
        holder.del.setOnClickListener(onClickListener);
        holder.slideItem.setOnClickListener(onClickListener);
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
        TextView artile;
        TextView createDate;

        TextView del;

        public RelativeLayout slideItem;

        public RecViewholder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_tv);
            artile = itemView.findViewById(R.id.article);
            createDate = itemView.findViewById(R.id.create_date);

            del = itemView.findViewById(R.id.close_tv);
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

    public interface ResponseRentInterface{
        void del(RentNeeds rent);
        void toInfo(RentNeeds rentNeeds);
    }
}

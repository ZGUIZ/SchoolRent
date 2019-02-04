package com.example.amia.schoolrent.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.amia.schoolrent.R;

public class ToolBarButton extends LinearLayout {

    private int beforeClickImage;
    private int afterCLickImage;
    private boolean isClicked;
    private String titleText;

    private ImageView icon;
    private TextView textView;

    private ClearButtonStatus clearButtonStatus;

    public ToolBarButton(Context context) {
        super(context);
        initView(context);
    }

    public ToolBarButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
        initView(context);
    }

    public ToolBarButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
        initView(context);
    }

    protected void initAttrs(Context context,AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.ToolBarButton);

        beforeClickImage = array.getResourceId(R.styleable.ToolBarButton_before_clicked,0);
        afterCLickImage = array.getResourceId(R.styleable.ToolBarButton_after_clicked,0);
        isClicked = array.getBoolean(R.styleable.ToolBarButton_isClicked,false);
        titleText = array.getString(R.styleable.ToolBarButton_title_text);
        array.recycle();//回收TypedArray
    }

    protected void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.bottom_button_layout,this,true);
        icon = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_view);

        textView.setText(titleText);
        setClicked(isClicked);
    }

    /**
     * 设置是否处于选中状态
     * @param isClicked
     */
    public void setClicked(boolean isClicked){
        this.isClicked = isClicked;
        if(isClicked){
            icon.setImageResource(afterCLickImage);
            textView.setTextColor(Color.rgb(8,138,152));
        } else {
            icon.setImageResource(beforeClickImage);
            textView.setTextColor(Color.rgb(0,0,0));
        }
    }

    @Deprecated
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    /**
     * 设置监听器
     * @param l
     * @param clearButtonStatus
     */
    public void setOnClickListener(OnClickListener l, ClearButtonStatus clearButtonStatus){
        super.setOnClickListener(l);
        this.clearButtonStatus = clearButtonStatus;
    }

    /**
     * 处理点击事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                clearButtonStatus.clearButtonStatus();
                setClicked(true);
                invalidate();//更新视图
                break;
        }

        return super.onTouchEvent(event);
    }
}

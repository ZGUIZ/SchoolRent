package com.example.amia.schoolrent.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.amia.schoolrent.R;

public class LogoView extends View {
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private int bgcolor; //背景颜色

    public LogoView(Context context) {
        this(context,null);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initAttrs(AttributeSet attrs){
        mPaint=new Paint();
        mWidth=300;
        mHeight=300;
        if(attrs==null){
            bgcolor=Color.WHITE;
            return;
        }
        TypedArray array=getContext().obtainStyledAttributes(attrs,R.styleable.LogoView);
        bgcolor=array.getColor(R.styleable.LogoView_bgColor,Color.GRAY);
        array.recycle();//回收TypedArray
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas){
        mPaint.setColor(Color.WHITE);
        //mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(20);
        mPaint.setAntiAlias(true);
        int fontSize = 200;
        mPaint.setTextSize(fontSize);
        canvas.drawText("租",(mWidth-fontSize)/2,mHeight-fontSize/10,mPaint);
        super.onDraw(canvas);
    }
}

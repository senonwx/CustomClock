package com.example.administrator.customclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/4/5.
 */

public class ClockView extends View {

    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width,height);
        mHourLine = (int) (width/2*0.5);
        mMinuateLine = (int)(width/2*0.6);
        mSecondLine = (int)(width/2*0.7);
    }

    private Paint mPaint;//初始化画笔
    private int width = 400;//控件宽度
    private int height = 400;//控件高度
    private int padding = 5;//设置边距
    private void initPaint() {
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#666666"));
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        drawCircle(canvas);
        drawScale(canvas);
        drawPointer(canvas);
        drawStr(canvas);
        mHandler.sendEmptyMessage(0);
    }
    //绘制表盘
    private void drawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width/2,height/2,width/2-padding,mPaint);
    }
    //绘制刻度
    private void drawScale(Canvas canvas){
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 12; i++) {
            if(i % 3 == 0)
                canvas.drawLine(width/2,padding,height/2,padding+4+20,mPaint);
            else
                canvas.drawLine(width/2,padding,height/2,padding+4+10,mPaint);
            canvas.rotate(30,width/2,height/2);
        }
    }
    private Calendar mCalendar;
    private int mHour,mMinute,mSecond;
    private float mDegrees;
    private int mHourLine,mMinuateLine,mSecondLine;
    //绘制指针
    private void drawPointer(Canvas canvas){
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mSecond = mCalendar.get(Calendar.SECOND);
        //时针
        mDegrees = mHour * 30 + mMinute / 2;
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(6);
        canvas.save();
        canvas.rotate(mDegrees,width/2,height/2);
        canvas.drawLine(width/2,height/2,width/2,height/2-mHourLine,mPaint);
        canvas.restore();
        //分针
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(4);
        mDegrees = mMinute * 6 + mSecond / 10;
        canvas.save();
        canvas.rotate(mDegrees,width/2,height/2);
        canvas.drawLine(width/2,height/2,width/2,height/2-mMinuateLine,mPaint);
        canvas.restore();
        //秒针
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.GREEN);
        mDegrees = mSecond * 6;
        canvas.save();
        canvas.rotate(mDegrees,width/2,width/2);
        canvas.drawLine(width/2,height/2,width/2,height/2-mSecondLine,mPaint);
        canvas.restore();
        mPaint.setColor(Color.BLACK);
    }
    //绘制文字
    private void drawStr(Canvas canvas){
        mPaint.setTextSize(40);
        StringBuffer sb = new StringBuffer();
        if(mHour<10){
            sb.append("0").append(String.valueOf(mHour)).append(":");
        }else{
            sb.append(String.valueOf(mHour)).append(":");
        }
        if(mMinute<10){
            sb.append("0").append(String.valueOf(mMinute)).append(":");
        }else{
            sb.append(String.valueOf(mMinute)).append(":");
        }
        if(mSecond<10){
            sb.append("0").append(String.valueOf(mSecond));
        }else{
            sb.append(String.valueOf(mSecond));
        }
        String time = sb.toString();
        int strW = (int) mPaint.measureText(time);
        canvas.drawText(time,width/2-strW/2,height/2+60,mPaint);
    }
    //handler发送实时消息
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };
}

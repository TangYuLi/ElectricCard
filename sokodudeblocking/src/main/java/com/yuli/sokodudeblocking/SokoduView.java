package com.yuli.sokodudeblocking;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class SokoduView extends View {

    private Paint mPaint;

    private float child_radius = 0;

    public float getChild_radius() {
        return child_radius;
    }

    public void setChild_radius(float child_radius) {
        this.child_radius = child_radius;
    }

    public SokoduView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SokoduView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //load Attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SokoduView);
        child_radius = typedArray.getDimension(R.styleable.SokoduView_child_radius, child_radius);
        typedArray.recycle();
    }

    //当没有新属性时使用的构造方法
    public SokoduView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("widthMeasureSpec = "+widthMeasureSpec);
        System.out.println("heightMeasureSpec = "+heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //首先规定一个九宫格外框架
        //获取外框的长和宽
        float outBorder_width = getWidth();

        //将长度以比例2:1:2:1:2分割
        child_radius = outBorder_width / 8.0f * 2;
        float space = outBorder_width / 8.0f;
        float deviation = child_radius + space;
        float x = child_radius / 2.0f;
        float y = x / 2.0f;

        //画同心圆
        //描边黑
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                canvas.drawCircle(x + i * deviation, x + j * deviation, x, mPaint);

        //填充外圆内部为灰
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                canvas.drawCircle(x + i * deviation, x + j * deviation, x, mPaint);

        //内圆内部和边均为黑
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                canvas.drawCircle(x + i * deviation, x + j * deviation, y, mPaint);
    }
}

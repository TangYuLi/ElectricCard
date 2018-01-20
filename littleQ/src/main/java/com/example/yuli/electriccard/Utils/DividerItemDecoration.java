package com.example.yuli.electriccard.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by YULI on 2017/9/5.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public int orientation;
    public Drawable mDivider;
    public int mDividerHeight;
    public int mDividerWidth;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    public static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    public DividerItemDecoration(Context context,int orientation){
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        if (mDivider!=null){
            mDividerHeight = mDivider.getIntrinsicHeight();
            mDividerWidth = mDivider.getIntrinsicWidth();
        }
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation){
        if (orientation!=HORIZONTAL_LIST && orientation!=VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (orientation==HORIZONTAL_LIST)drawHorizontalDivider(c,parent);
        else drawVerticalDivider(c,parent);
    }

    public void drawHorizontalDivider(Canvas c,RecyclerView parent){
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth()-parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i=0;i<childCount;i++){
            final View item = parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams)item.getLayoutParams();
            final int top = item.getBottom()+layoutParams.bottomMargin;
            final int bottom = top+mDividerHeight;
            if (mDivider!=null) {
                mDivider.setBounds(left,top,right,bottom);
                mDivider.draw(c);
            }
        }
    }

    public void drawVerticalDivider(Canvas c,RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight()-parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i=0;i<childCount;i++){
            final View item = parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams)item.getLayoutParams();
            final int left = item.getRight()+layoutParams.rightMargin;
            final int right = left+mDividerWidth;
            if (mDivider!=null){
                mDivider.setBounds(left,top,right,bottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}

package com.smarter.onejoke.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by panl on 15/3/4.
 */
public class AlbumViewPager extends ViewPager implements OnChildMovingListener {
    /**  当前子控件是否处理拖动状态  */
    private boolean mChildIsBeingDragged=false;

    public AlbumViewPager(Context context) {
        super(context);
    }

    public AlbumViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(mChildIsBeingDragged)
            return false;
        return super.onInterceptTouchEvent(arg0);
    }
    @Override
    public void startDrag() {
        // TODO Auto-generated method stub
        mChildIsBeingDragged=true;
    }


    @Override
    public void stopDrag() {
        // TODO Auto-generated method stub
        mChildIsBeingDragged=false;
    }

}

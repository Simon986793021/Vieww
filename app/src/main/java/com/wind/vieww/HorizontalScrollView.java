package com.wind.vieww;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by zhangcong on 2017/12/6.
 */

public class HorizontalScrollView extends ViewGroup {
    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;

    private int mLastX=0;
    private int mLastY=0;

    private int mLastXIntercept=0;
    private int mLastYIntercept=0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public HorizontalScrollView(Context context) {
        super(context);
        init();
    }


    public HorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        if (mScroller==null){
            mScroller=new Scroller(getContext());
            mVelocityTracker=VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted=false;
        int x= (int) ev.getX();
        int y= (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercepted=false;
                if (!mScroller.isFinished()){
                    //停止滑动
                    mScroller.abortAnimation();
                    intercepted=true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int delatX=x-mLastXIntercept;
                int delatY=y-mLastYIntercept;
                if (Math.abs(delatX)>Math.abs(delatY)){
                    intercepted=true;
                }
                else{
                    intercepted=false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted=false;
                break;
            default:
                break;
        }
        mLastX=x;
        mLastY=y;
        mLastYIntercept=y;
        mLastXIntercept=x;

        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x= (int) event.getX();
        int y= (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX=x-mLastX;
                int deltaY=y-mLastY;
                scrollBy(-deltaX,0);
                break;
            case MotionEvent.ACTION_UP:
                int scrollX=getScrollX();
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity=mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) >= 50){
                    mChildIndex=xVelocity>0?mChildIndex-1:mChildIndex+1;
                }
                else {
                    mChildIndex=(scrollX+mChildWidth/2)/mChildWidth;
                }
                mChildIndex=Math.max(0,Math.min(mChildIndex,mChildrenSize-1));
                int dx=mChildIndex*mChildWidth-scrollX;
                smoothScrollBy(dx,0);
                mVelocityTracker.clear();
                break;
            default:
                break;
        }
        mLastY=y;
        mLastY=x;
        return true;
    }
    private void smoothScrollBy(int dx,int dy){
        mScroller.startScroll(getScrollX(),0,dx,0,500);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth=0;
        int measureHeight=0;
        final int childCount=getChildCount();
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int widthSpaceSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthSpaceMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightSpaceMode=MeasureSpec.getMode(heightMeasureSpec);

        if (childCount==0) {
            setMeasuredDimension(0,0);
        }
        else if (widthSpaceMode==MeasureSpec.AT_MOST&&heightSpaceMode==MeasureSpec.AT_MOST){
            final View childView=getChildAt(0);
            measureWidth=childView.getMeasuredWidth()*childCount;
            measureHeight=childView.getMeasuredHeight();
            setMeasuredDimension(measureWidth,measureHeight);
        }
        else if (heightSpaceMode==MeasureSpec.AT_MOST){
            final View childView=getChildAt(0);
            measureHeight=childView.getMeasuredHeight();
            setMeasuredDimension(widthSpaceSize,measureHeight);
        }
        else if (widthSpaceMode==MeasureSpec.AT_MOST){
            final View childView=getChildAt(0);
            measureWidth=childView.getMeasuredWidth()*childCount;
            setMeasuredDimension(measureWidth,heightSpaceSize);
        }

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int childLeft=0;
        final int childCount=getChildCount();
        mChildrenSize=childCount;
        for (int x =0; x<childCount;x++){
            final View childView=getChildAt(x);
            if (childView.getVisibility()!=GONE){
                final int childWidth=childView.getMeasuredWidth();
                mChildWidth=childWidth;
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft+=childWidth;
            }
        }
    }


}

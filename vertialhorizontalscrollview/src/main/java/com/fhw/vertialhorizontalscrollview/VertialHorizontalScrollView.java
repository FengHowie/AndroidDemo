package com.fhw.vertialhorizontalscrollview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by fenghanwei on 2018/3/20.
 * 上下左右滑动view
 */

public class VertialHorizontalScrollView extends ListView implements View.OnTouchListener {

    private long longClickTime = (long) (1 *1000);// >
    private int horizontalMinSmollValue = 5;
    private List<HorizontalScrollView> dispatchHorizontalScrollView = new ArrayList<HorizontalScrollView>();

    public VertialHorizontalScrollView(Context context) {
        this(context,null);
    }
    private final static String TAG = VertialHorizontalScrollView.class.getSimpleName();
    public VertialHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VertialHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOnTouchListener(this);
    }

    @Override
    public void addHeaderView(View v) {
        super.addHeaderView(v);
        HorizontalScrollView mHorizontalScrollView = foundHorizontalScrollDispatchView(v);
        if (mHorizontalScrollView != null) {
            addDispatchHorizontalScrollView(mHorizontalScrollView);
        }
    }

    @Override
    public void addFooterView(View v) {
        super.addFooterView(v);
        HorizontalScrollView mHorizontalScrollView = foundHorizontalScrollDispatchView(v);
        if (mHorizontalScrollView != null) {
            addDispatchHorizontalScrollView(mHorizontalScrollView);
        }
    }

    private int getHorizontalScrollViewX(){

        int size = dispatchHorizontalScrollView.size();
        HashMap<Integer,Integer> map = new HashMap<>();
        for (int i =0 ; i < size; i++){
            int scrollX = dispatchHorizontalScrollView.get(i).getScrollX();

            if (map.containsKey(scrollX)){
                int count = map.get(scrollX)+1;
                map.put(scrollX,count);
            }else{
                map.put(scrollX,1);
            }
        }
        int mostScrollXCount = 0;
        int mostScrollX = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > mostScrollXCount){
                mostScrollXCount = entry.getValue();
                mostScrollX = entry.getKey();
            }
        }

        return mostScrollX;
    }



    /**
     * 找到布局中的HorizontalScrollView
     *
     * @param v
     * @return HorizontalScrollView
     */
    private HorizontalScrollView foundHorizontalScrollDispatchView(View v) {
        if (v instanceof ViewGroup) {
            int childCound = ((ViewGroup) v).getChildCount();
            for (int i = childCound - 1; i >= 0; i--) {
                if (((ViewGroup) v).getChildAt(i) instanceof ViewGroup) {
                    HorizontalScrollView horizontalScrollDispatchView;
                    horizontalScrollDispatchView = foundHorizontalScrollDispatchView(((ViewGroup) v).getChildAt(i));
                    if (horizontalScrollDispatchView != null) {
                        return horizontalScrollDispatchView;
                    }
                } else {
                    if (((ViewGroup) v).getChildAt(i) instanceof HorizontalScrollView) {
                        return (HorizontalScrollView) ((ViewGroup) v).getChildAt(i);
                    }
                }
            }
        }
        if (v instanceof HorizontalScrollView) {
            return (HorizontalScrollView) v;
        }
        return null;
    }


    // FIXME: 2018/3/21 什么时机释放 dispatchHorizontalScrollView item
    public void addDispatchHorizontalScrollView(final HorizontalScrollView horizontalScrollView) {
        //Log.d(TAG, "addDispatchHorizontalScrollView size = "+dispatchHorizontalScrollView.size());

        postDelayed(new Runnable() {
            @Override
            public void run() {
                //同步滑动位置
                int scrollX = getHorizontalScrollViewX();
                //Log.d(TAG, "addDispatchHorizontalScrollView scrollX = "+scrollX);
                if (!(dispatchHorizontalScrollView == null || dispatchHorizontalScrollView.size() == 0)){
                    int size = dispatchHorizontalScrollView.size();
                    for (int i =0 ; i < size; i++){
                        dispatchHorizontalScrollView.get(i).scrollTo(scrollX, 0);
                    }
                }
            }
        },55);
        /**
         * 如果手指已经按下屏幕未松开，需要设置新加入的horizontalScrollView action down，这才能响应后续的action move
         * 解决action move 异常Invalid pointerId=-1 in onTouchEvent
         */
        if (actionDownEvent != null) {
            horizontalScrollView.onInterceptTouchEvent(actionDownEvent);
            horizontalScrollView.onTouchEvent(actionDownEvent);
        }

        dispatchHorizontalScrollView.add(horizontalScrollView);
    }



    public void removeDispatchHorizontalScrollView(HorizontalScrollView horizontalScrollView) {
        dispatchHorizontalScrollView.remove(horizontalScrollView);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    private float x;
    private float y;
    private int lastMoveViewType = -1;
    private long downTimeMill = 0;
    private boolean ignoreTouchEvent = false;
    private MotionEvent actionDownEvent;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ignoreTouchEvent = false;
                actionDownEvent = MotionEvent.obtainNoHistory(event);
                downTimeMill = System.currentTimeMillis();
                lastMoveViewType = -1;
                x = event.getRawX();
                y = event.getRawY();
                if (dispatchHorizontalScrollView != null && dispatchHorizontalScrollView.size() > 0) {
                    int size = dispatchHorizontalScrollView.size();
                    for (int i = size-1;i >=0;i--){
                        dispatchHorizontalScrollView.get(i).onTouchEvent(event);
                    }
                }
                postDelayed(touchDownLongClickRunable,longClickTime);
                break;

            case MotionEvent.ACTION_MOVE:
                if (!(Math.abs(x - event.getRawX()) < horizontalMinSmollValue && Math.abs(y - event.getRawY()) < horizontalMinSmollValue)) {
                    removeCallbacks(touchDownLongClickRunable);
                }
                if (event.getPointerCount() > 1){
                    ignoreTouchEvent = true;
                    Log.d(TAG, "MotionEvent press count > 1");

                }
                if (!ignoreTouchEvent && dispatchHorizontalScrollView.size() > 0){
                    if (lastMoveViewType == 1 || (Math.abs(x - event.getRawX()) >= horizontalMinSmollValue)) {
                        lastMoveViewType = 1;
                        int size = dispatchHorizontalScrollView.size();
                        for (int i = size-1;i >=0;i--){
                            dispatchHorizontalScrollView.get(i).onTouchEvent(event);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 1) {
                    ignoreTouchEvent = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                removeCallbacks(touchDownLongClickRunable);
                break;
            case MotionEvent.ACTION_UP:
                actionDownEvent = null;
                removeCallbacks(touchDownLongClickRunable);
                long upTimeMill = System.currentTimeMillis();
                if (upTimeMill - downTimeMill < longClickTime) {
                    if (Math.abs(x - event.getRawX()) < horizontalMinSmollValue && Math.abs(y - event.getRawY()) < horizontalMinSmollValue) {
                        //Toast.makeText(getContext(),"on touch click",Toast.LENGTH_SHORT).show();
                        int size = dispatchHorizontalScrollView.size();
                        for (int i = size-1;i >=0;i--){
                            dispatchHorizontalScrollView.get(i).onTouchEvent(event);


                        }
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    private View isTouchPointInView(View view, float x, float y) {
        if (view == null) {
            return null;
        }
        int[] location = new int[2];
        if (view instanceof ViewGroup){
            int size = ((ViewGroup) view).getChildCount();
            for (int i = size-1 ; i >=0; i--){
                View returnView =  isTouchPointInView(((ViewGroup) view).getChildAt(i),x,y);
                if (returnView != null){
                    return returnView;
                }
            }
        }else{
            view.getLocationOnScreen(location);
            int left = location[0];
            int top = location[1];
            int right = left + view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();

            /*Log.d(TAG, "isTouchPointInView  left =  "+left +
                "  right = "+ right+
                "   top = " + top +
                "   bottom = " + bottom);*/

            //view.isClickable() &&
            if (y >= top && y <= bottom && x >= left
                    && x <= right) {
                return view;
            }
        }

        return null;
    }

    /**
     * 长按runnable
     */
    private Runnable touchDownLongClickRunable = new Runnable() {

        @Override
        public void run() {
            int size = dispatchHorizontalScrollView.size();
            for (int i = 0;i < size;i++){
                View touchView = isTouchPointInView(dispatchHorizontalScrollView.get(i),x,y);
                if (touchView != null){
                    touchView.performLongClick();

                }
            }
        }
    };

    public void setLongClickTime(long longClickTime) {
        this.longClickTime = longClickTime;
    }
   

    public void setHorizontalMinSmollValue(int horizontalMinSmollValue) {
        this.horizontalMinSmollValue = horizontalMinSmollValue;
    }
}

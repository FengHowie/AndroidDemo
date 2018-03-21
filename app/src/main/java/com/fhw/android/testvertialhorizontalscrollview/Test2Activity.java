package com.fhw.android.testvertialhorizontalscrollview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fhw.android.R;
import com.fhw.vertialhorizontalscrollview.VertialHorizontalScrollView;

/**
 * Created by fenghanwei on 2018/3/20.
 * 上下左右滑动view  固定头部，可变滑动个数
 * 设置触摸触发长按的时间，小于长按时间是点击
 * 设置水平滑动多少触发水平滑动，单位像素
 */

public class Test2Activity extends AppCompatActivity {
    private final static String TAG = Test2Activity.class.getSimpleName();

    private Toolbar mToolbar;
    private VertialHorizontalScrollView mListView;
    private Context mContext;

    private String[] titleValues= {"类别1","类别2","类别3","类别4","类别5","类别6","类别7","类别8","类别9","类别10","类别11","类别12","类别13"};//每列标题
    private int contentWidths[] = {40,50,60,70,60,50,40,50,60,70,60,50,40};//没列宽度
    private int stableNum = 3;//头部固定不滑动的数量
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test2);
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.test2);
        mListView = findViewById(R.id.lv_list);

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);


        {//设置固定头部start
            createItemView(titleValues.length, stableNum,
                    (LinearLayout) findViewById(R.id.ly_parent),
                    (HorizontalScrollView) findViewById(R.id.horizontalScrollView));
            setItemValue(stableNum, titleValues,
                    (LinearLayout) findViewById(R.id.ly_parent),
                    (HorizontalScrollView) findViewById(R.id.horizontalScrollView));
            //mListView.addHeaderView(headerView);
            mListView.addDispatchHorizontalScrollView((HorizontalScrollView) findViewById(R.id.horizontalScrollView));
        }//设置固定头部end

        mListView.setLongClickTime((long) (0.5*1000));//设置触摸触发长按的时间，小于长按时间是点击
        mListView.setHorizontalMinSmollValue(5);//设置水平滑动多少触发水平滑动，单位像素
        mListView.setAdapter(new Test1Adapter());


    }


    //可变滑动个数start
    private void createItemView(int totalNum ,int stableNum,LinearLayout lyParent,HorizontalScrollView horizontalScrollView){
        for (int i = 0 ; i < stableNum; i++){
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(mContext,contentWidths[i]),LinearLayout.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            lyParent.addView(textView,0);
        }
        LinearLayout horLinearLayout = (LinearLayout) horizontalScrollView.getChildAt(0);
        for (int i = stableNum; i < totalNum; i++){
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(mContext,contentWidths[i]),LinearLayout.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            horLinearLayout.addView(textView);
        }
    }
    private void setItemValue(int stableNum,String[] values,LinearLayout lyParent,HorizontalScrollView horizontalScrollView){
        for (int i = 0 ; i < stableNum; i++){
            TextView textView = (TextView) lyParent.getChildAt(i);
            textView.setText(values[i]+"");
        }
        LinearLayout horLinearLayout = (LinearLayout) horizontalScrollView.getChildAt(0);
        for (int i = stableNum; i < values.length; i++){
            TextView textView = (TextView) horLinearLayout.getChildAt(i-stableNum);
            textView.setText(values[i]+"");
        }
    }

    private String[] createValues(){
        int num = titleValues.length;
        String[] values = new String[num];
        for (int i = 0 ; i < num; i++){
            values[i] = titleValues[i] + "-"+(i+1);
        }
        return values;
    }
    //可变滑动个数end


    public class Test1Adapter extends BaseAdapter{
        private Handler handler = new Handler();
        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_test2, null);
                viewHolder = new ViewHolder(convertView);
                createItemView(titleValues.length,stableNum,viewHolder.lyParent,viewHolder.horizontalScrollView);
                mListView.addDispatchHorizontalScrollView(viewHolder.horizontalScrollView);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            setItemValue(stableNum,createValues(),viewHolder.lyParent,viewHolder.horizontalScrollView);
            LinearLayout layout = ((LinearLayout)viewHolder.horizontalScrollView.getChildAt(0));

            int size = layout.getChildCount();
            for (int i = 0 ; i < size; i++){
                final int scrollY = i;

                layout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick x = "+(position+1)+"  y = "+ (scrollY+1));
                        Toast.makeText(mContext,"点击了 horizontalScrollview"+(position+1)+"行"+(scrollY+1)+"列",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            for (int i = 0 ; i < size; i++){
                final int scrollY = i;

                layout.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        Log.d(TAG, "onLongClick x = "+(position+1)+"  y = "+ (scrollY+1));
                        Toast.makeText(mContext,"长按了 horizontalScrollview "+(position+1)+"行"+(scrollY+1)+"列",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            return convertView;
        }
        public class ViewHolder{
            public HorizontalScrollView horizontalScrollView;
            public LinearLayout lyParent;
            private ViewHolder(){}
            public  ViewHolder(View v){
                lyParent = v.findViewById(R.id.ly_parent);
                horizontalScrollView = v.findViewById(R.id.horizontalScrollView);
                v.setTag(this);
            }
        }
    }



    private int dp2px(Context context,int dpValue){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }
}

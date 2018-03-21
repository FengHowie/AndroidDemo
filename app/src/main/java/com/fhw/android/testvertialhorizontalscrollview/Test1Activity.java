package com.fhw.android.testvertialhorizontalscrollview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.fhw.android.R;
import com.fhw.vertialhorizontalscrollview.VertialHorizontalScrollView;

/**
 * Created by fenghanwei on 2018/3/20.
 * 上下左右滑动view  固定滑动个数
 */

public class Test1Activity extends AppCompatActivity {
    private final static String TAG = Test1Activity.class.getSimpleName();

    private Toolbar mToolbar;
    private VertialHorizontalScrollView mListView;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test1);
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.test1);
        mListView = findViewById(R.id.lv_list);

        mListView.addHeaderView(getLayoutInflater().inflate(R.layout.item_test1,null));

        mListView.setAdapter(new Test1Adapter());
    }

    public class Test1Adapter extends BaseAdapter{

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_test1, null);
                viewHolder = new ViewHolder(convertView);
                mListView.addDispatchHorizontalScrollView(viewHolder.horizontalScrollView);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            LinearLayout layout = ((LinearLayout)viewHolder.horizontalScrollView.getChildAt(0));
            int size = layout.getChildCount();
            for (int i = 0 ; i < size; i++){
                final int scrollY = i;

                layout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick x = "+(position+1)+"  y = "+ (scrollY+1));
                        Toast.makeText(mContext,"点击了"+(position+1)+"行"+(scrollY+1)+"列",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            for (int i = 0 ; i < size; i++){
                final int scrollY = i;

                layout.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        Log.d(TAG, "onLongClick x = "+(position+1)+"  y = "+ (scrollY+1));
                        Toast.makeText(mContext,"长按了"+(position+1)+"行"+(scrollY+1)+"列",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            return convertView;
        }
        public class ViewHolder{
            public HorizontalScrollView horizontalScrollView;
            private ViewHolder(){}
            public  ViewHolder(View v){
                horizontalScrollView = v.findViewById(R.id.horizontalScrollView);
                v.setTag(this);
            }
        }
    }
}

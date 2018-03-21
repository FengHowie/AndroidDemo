package com.fhw.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fhw.android.testvertialhorizontalscrollview.Test1Activity;
import com.fhw.android.testvertialhorizontalscrollview.Test2Activity;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

    }


    public void test1(View view) {
        startActivity(new Intent(mContext, Test1Activity.class));

    }

    public void test2(View view) {
        startActivity(new Intent(mContext, Test2Activity.class));
    }
}

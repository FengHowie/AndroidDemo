package com.fhw.android.testvertialhorizontalscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class InterceptTouchContainer extends LinearLayout {

	private boolean shouldInterceptTouchEvent = true;
	public InterceptTouchContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InterceptTouchContainer(Context context) {
		super(context);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (shouldInterceptTouchEvent) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public boolean isShouldInterceptTouchEvent() {
		return shouldInterceptTouchEvent;
	}

	public void setShouldInterceptTouchEvent(boolean shouldInterceptTouchEvent) {
		this.shouldInterceptTouchEvent = shouldInterceptTouchEvent;
	}
	
	
}

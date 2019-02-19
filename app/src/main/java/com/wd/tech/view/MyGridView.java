package com.wd.tech.view;

import android.content.Context;
import android.widget.GridView;

public class MyGridView extends GridView {
        public MyGridView(Context context) {
            super(context);
        }
        //设置不滚动
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }
}

package com.wd.tech.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wd.tech.R;

/**
 * 积分兑换失败弹出赚积分   PopupWindow
 *
 * @author lmx
 * @date 2019/3/1
 */
public class PayExchangePopWindowF extends PopupWindow {

    private ImageView mExchageImgDel;
    private TextView mCancel, mEarn;
    private View mMenuView;

    public PayExchangePopWindowF(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMenuView = inflater.inflate(R.layout.pay_way_exchange_f, null);
        mExchageImgDel = (ImageView) mMenuView.findViewById(R.id.pay_way_exchage_f_del);
        mCancel = (TextView) mMenuView.findViewById(R.id.pay_way_exchage_f_cancel);
        mEarn = (TextView) mMenuView.findViewById(R.id.pay_way_exchage_f_earn);

        //设置按钮监听
        mExchageImgDel.setOnClickListener(itemsOnClick);
        mCancel.setOnClickListener(itemsOnClick);
        mEarn.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xbffffff);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pay_way_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}

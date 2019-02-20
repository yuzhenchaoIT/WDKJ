package com.wd.tech.core;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.gson.Gson;
import com.wd.tech.util.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class WDFragment extends Fragment {
	public Gson mGson = new Gson();
	public SharedPreferences mShare = WDApplication.getShare();

	private Unbinder unbinder;
	private View view;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 每次ViewPager要展示该页面时，均会调用该方法获取显示的View
		long time = System.currentTimeMillis();
		view = inflater.inflate(getLayoutId(),container,false);
		unbinder = ButterKnife.bind(this, view);
		initView();
		LogUtils.e(this.toString()+"页面加载使用："+(System.currentTimeMillis()-time));
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

		@Override
	public void onResume() {
		super.onResume();
		//if (!MTStringUtils.isEmpty(getPageName()))
			/*if (Build.VERSION.SDK_INT >= 23) {
				if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(getActivity(), new String[]{
							Manifest.permission.WRITE_EXTERNAL_STORAGE,
							Manifest.permission.READ_PHONE_STATE,
							Manifest.permission.ACCESS_NETWORK_STATE,
							Manifest.permission.CHANGE_WIFI_STATE,
							Manifest.permission.ACCESS_WIFI_STATE,
							Manifest.permission.ACCESS_COARSE_LOCATION,
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
					}, 100);
				}
			}*/
			/*Log.i("abb", "onResume: "+getPageName());
			if (!TextUtils.isEmpty(getPageName())){

				MobclickAgent.onPageEnd(getPageName());// 统计页面
			}*/
	}

	/*@Override
	public void onPause() {
		super.onPause();
		//if (!MTStringUtils.isEmpty(getPageName()))
		if (!TextUtils.isEmpty(getPageName())){
			MobclickAgent.onPageEnd(getPageName());// 统计页面
		}

	}*/

	/**
	 * 设置页面名字 用于友盟统计
	 */
	public abstract String getPageName();
	/**
	 * 设置layoutId
	 * @return
	 */
	protected abstract int getLayoutId();

	/**
	 * 初始化视图
	 */
	protected abstract void initView();
	/*@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 100) {
			UIUtils.showToastSafe("权限打开");
		}
	}*/
}

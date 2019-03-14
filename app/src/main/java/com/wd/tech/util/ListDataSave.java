package com.wd.tech.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wd.tech.bean.CommunityListBean;
import com.wd.tech.bean.HomeListBean;

import java.util.ArrayList;
import java.util.List;

public class ListDataSave {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public ListDataSave(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public <T> List<CommunityListBean> getDataList(String tag) {
        List<CommunityListBean> datalist = new ArrayList<CommunityListBean>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<CommunityListBean>>() {
        }.getType());
        return datalist;

    }

    /**
     * 获取List
     *
     * @param tag1
     * @return
     */
    public <T> List<HomeListBean> getDataList1(String tag1) {
        List<HomeListBean> datalist1 = new ArrayList<HomeListBean>();
        String strJson = preferences.getString(tag1, null);
        if (null == strJson) {
            return datalist1;
        }
        Gson gson = new Gson();
        datalist1 = gson.fromJson(strJson, new TypeToken<List<HomeListBean>>() {
        }.getType());
        return datalist1;

    }

}

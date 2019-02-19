package com.wd.tech.view;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.core.WDActivity;
import com.wd.tech.util.StringUtils;

import butterknife.OnTextChanged;

public class AddCircleActivity extends WDActivity {


    private TextView textSum;
    private EditText editTex;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_adcircle;
    }
    @OnTextChanged(value = R.id.id_editor_detail, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void editTextDetailChange(Editable editable) {
        int detailLength = editable.length();
        textSum.setText(detailLength + "/300");
//        if (detailLength == 139) {
//            islMaxCount = true;
//        }
//        // 不知道为什么执行俩次，所以增加一个标识符去标识
//        if (detailLength == 140 && islMaxCount) {
//            UIHelper.getShortToast(self, (String) StringUtils.getResourceContent(self, Convention.RESOURCE_TYPE_STRING, R.string.string_editor_detail_input_limit));
//            islMaxCount = false;
//        }
        if (detailLength==300){
            Toast.makeText(this, "别输入了,", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void initView() {
        textSum = (TextView) findViewById(R.id.id_editor_detail_font_count);
        editTex = (EditText) findViewById(R.id.id_editor_detail);


    }

    @Override
    protected void destoryData() {

    }
}

package com.xin.launcher.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import xin.framework.base.activity.XinActivity;
import xin.framework.utils.android.view.ViewFinder;
import xin.framework.utils.android.view.compatibility.title.StatusBarUtil;
import xin.framework.widget.title.CommonTitleBar;
import xin.ui.launcher.R;

/**
 * 作者：xin on 2018/9/27 14:00
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class ConstructionActivity extends XinActivity {

    private String titleName;

    public static void launcher(Context context, String name) {
        Intent intent = new Intent(context, ConstructionActivity.class);
        intent.putExtra("title_name", name);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    public void prepareData() {
        super.prepareData();
        titleName = getIntent().getStringExtra("title_name");
    }

    @Override
    public void bindUI(View rootView) {
        CommonTitleBar mTitleBar = ViewFinder.find(rootView, R.id.title_bar);
        StatusBarUtil.immersive(this);
        mTitleBar.getCenterTextView().setText(titleName);
        mTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (CommonTitleBar.ACTION_LEFT_TEXT == action) {
                    finish();
                }
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.construction_activity;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        StatusBarUtil.darkMode(this, Color.parseColor("#DFE1E0"), 1);
    }
}

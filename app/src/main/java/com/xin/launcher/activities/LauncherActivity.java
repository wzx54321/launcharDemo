package com.xin.launcher.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xin.launcher.fragments.HomeAppFragment;
import com.xin.launcher.fragments.LeftContentFragment;

import java.util.ArrayList;
import java.util.List;

import xin.framework.base.activity.XinActivity;
import xin.framework.base.fragment.XinFragment;
import xin.framework.utils.android.PermissionUtil;
import xin.framework.utils.android.SysUtils;
import xin.framework.utils.android.view.compatibility.title.StatusBarUtil;
import xin.ui.launcher.R;

/**
 * 作者：xin on 2018/9/20 10:13
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class LauncherActivity extends XinActivity {

    private List<XinFragment> fragments;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        SysUtils.hideBottomUIMenu(this);
        StatusBarUtil.darkMode(this, false);
        StatusBarUtil.immersive(getWindow());
    }

    @Override
    public void prepareData() {
        super.prepareData();
        HomeAppFragment mHomeAppFragment = new HomeAppFragment();
        fragments = new ArrayList<>();
        fragments.add(new LeftContentFragment());
        fragments.add(mHomeAppFragment);
    }

    @Override
    public void bindUI(View rootView) {
        ViewPager mViewPager = findViewById(R.id.page_root);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mViewPager.setCurrentItem(1, false);


    }

    @Override
    public void initData(Bundle savedInstanceState) {
        PermissionUtil.microphone(new PermissionUtil.PermissionCallback() {
            @Override
            public void onRequestPermissionSuccess() {
            }

            @Override
            public void onRequestPermissionFailure() {
            }

            @Override
            public void onRequestError() {
            }
        }, new RxPermissions(this));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    public Object newP() {
        return null;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        SysUtils.hideBottomUIMenu(this);
        StatusBarUtil.darkMode(this, false);
        StatusBarUtil.immersive(getWindow());
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }


}

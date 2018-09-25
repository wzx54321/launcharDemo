package com.panxiaohe.springboard.demo.fragments;

import android.os.Bundle;
import android.view.View;

import com.panxiaohe.springboard.demo.adapter.MyAdapter;
import com.panxiaohe.springboard.demo.bean.MyAppItem;
import com.panxiaohe.springboard.demo.P.PHome;
import com.panxiaohe.springboard.demo.R;
import com.panxiaohe.springboard.library.DotView;
import com.panxiaohe.springboard.library.FavoritesItem;
import com.panxiaohe.springboard.library.MenuView;
import com.panxiaohe.springboard.library.SpringboardView;

import java.util.ArrayList;

import xin.framework.base.fragment.XinFragment;
import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.android.view.ViewFinder;

/**
 * 作者：xin on 2018/9/20 10:31
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class HomeAppFragment extends XinFragment<PHome>  {
    private ArrayList<MyAppItem> myApps;
    private DotView mIndicator;
    private MyAdapter myAdapter;

    @Override
    protected void initVariables(Bundle bundle) {

        myApps = new ArrayList<>(getP().getApps());
    }

    @Override
    protected void bindDataFirst() {

    }

    @Override
    public void bindUI(View rootView) {
        initVariables(null);

        //
        mIndicator = ViewFinder.find(rootView, R.id.page_indicator);
        //
        final MenuView springboard = ViewFinder.find(rootView, R.id.springboard);
        myAdapter = new MyAdapter(myApps);
        springboard.setAdapter(myAdapter);
        mIndicator.init();


        springboard.setOnItemClickListener(new SpringboardView.OnItemClickListener() {
            @Override
            public void onItemClick(FavoritesItem item) {
                MyAppItem myItem = (MyAppItem) item;
                // TODO 点击事件
            }
        });

        springboard.setOnPageChangedListener(new SpringboardView.OnPageChangedListener() {
            @Override
            public void onPageScroll(int from, int to) {
                Log.d("   scroll from page#" + from + " to page#" + to);
                mIndicator.setCurrentPage(to);
            }

            @Override
            public void onPageCountChange(int oldCount, int newCount) {
                Log.d("  page count has changed from #" + oldCount + " to #" + newCount);
                mIndicator.setPages(newCount);
                mIndicator.setCurrentPage(springboard.getmCurScreen());
            }
        });

        myAdapter.setDataCahngeListener(getP().getDataChangeListener());

        mIndicator.setPages(springboard.getPageCount());
        mIndicator.setCurrentPage(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public PHome newP() {
        return new PHome();
    }


    @Override
    public void onDestroyView() {
        if (myAdapter != null)
            myAdapter.setDataCahngeListener(null);
        super.onDestroyView();

    }



}

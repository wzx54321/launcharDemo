package com.panxiaohe.springboard.demo.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panxiaohe.springboard.demo.P.PHome;
import com.panxiaohe.springboard.demo.R;
import com.panxiaohe.springboard.demo.adapter.MyAdapter;
import com.panxiaohe.springboard.demo.bean.MyAppItem;
import com.panxiaohe.springboard.library.DotView;
import com.panxiaohe.springboard.library.FavoritesItem;
import com.panxiaohe.springboard.library.MenuView;
import com.panxiaohe.springboard.library.SpringboardView;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import xin.framework.base.fragment.XinFragment;
import xin.framework.hybrid.activity.CommWebViewActivity;
import xin.framework.hybrid.bean.WebOpenInfo;
import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.android.view.ViewFinder;

/**
 * 作者：xin on 2018/9/20 10:31
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class HomeAppFragment extends XinFragment<PHome> {
    private ArrayList<MyAppItem> myApps;
    private DotView mIndicator;
    private MyAdapter myAdapter;
    private LinearLayout mBottomLayout;
    private RelativeLayout mTitleLayout;
    private TextView mTitleText;
    private ArrayList<String> mTextArray;


    @Override
    protected void initVariables(Bundle bundle) {

        myApps = new ArrayList<>(getP().getApps());
        mTextArray = new ArrayList<>();
        mTextArray.add("公民");
        mTextArray.add("公务员");
        mTextArray.add("法人");
        mTextArray.add("程序员");

    }

    @Override
    protected void bindDataFirst() {

    }

    @SuppressLint("CheckResult")
    @Override
    public void bindUI(View rootView) {
        initVariables(null);

        //
        mBottomLayout = ViewFinder.find(rootView, R.id.bottom_layout);
        mTitleLayout = ViewFinder.find(rootView, R.id.title_bar);
        mTitleText = ViewFinder.find(rootView, R.id.title_text);
        View mBtnMore = ViewFinder.find(rootView, R.id.btn_more);
        mBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebOpenInfo info=new WebOpenInfo();
                info.setUrl("http://t200storemarket.zhengtoon.com/app/index.html#/");
                CommWebViewActivity.launcher(getContext(),info);
            }
        });

      //  setTitleViewTopPadding(mTitleLayout);
        getP().createBlurBg(getContext(), R.drawable.bg_blur1).subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) {
                mBottomLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
            }
        });
        getP().createBlurBg(getContext(), R.drawable.bg_blur3,15).subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) {
                mTitleLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
            }
        });
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
                mTitleText.setText(mTextArray.get(to));
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
        mTitleText.setText(mTextArray.get(0));
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

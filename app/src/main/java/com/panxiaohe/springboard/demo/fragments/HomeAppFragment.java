package com.panxiaohe.springboard.demo.fragments;

import android.os.Bundle;
import android.view.View;

import com.panxiaohe.springboard.demo.MyAdapter;
import com.panxiaohe.springboard.demo.MyButtonItem;
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
public class HomeAppFragment extends XinFragment {
    private ArrayList<MyButtonItem> buttons;
    private MyAdapter myAdapter;
    private DotView mIndicator;

    @Override
    protected void initVariables(Bundle bundle) {
        buttons = new ArrayList<>();
        buttons.add(new MyButtonItem("0", "24小时图书馆", R.drawable.icon_1));
        buttons.add(new MyButtonItem("1", "12345", R.drawable.icon_2));
        buttons.add(new MyButtonItem("2", "不动产状况", R.drawable.icon_3));
        buttons.add(new MyButtonItem("3", "车主服务", R.drawable.icon_4));
        buttons.add(new MyButtonItem("4", "城市停车", R.drawable.icon_5));
        buttons.add(new MyButtonItem("5", "电动车目录", R.drawable.icon_6));
        buttons.add(new MyButtonItem("6", "电梯查询", R.drawable.icon_7));
        buttons.add(new MyButtonItem("7", "电影娱乐", R.drawable.icon_8));
        buttons.add(new MyButtonItem("8", "公积金", R.drawable.icon_9));
        buttons.add(new MyButtonItem("9", "公园景区", R.drawable.icon_10));
        buttons.add(new MyButtonItem("10", "惠民资金", R.drawable.icon_11));
        buttons.add(new MyButtonItem("11", "机动车维修点", R.drawable.icon_12));
        buttons.add(new MyButtonItem("12", "健康证查询", R.drawable.icon_13));
        buttons.add(new MyButtonItem("13", "交警服务", R.drawable.icon_14));
        buttons.add(new MyButtonItem("14", "交通出行", R.drawable.icon_15));
        buttons.add(new MyButtonItem("15", "教育缴费", R.drawable.icon_16));
        buttons.add(new MyButtonItem("16", "景点门票", R.drawable.icon_17));
        buttons.add(new MyButtonItem("17", "看病就医", R.drawable.icon_18));
        buttons.add(new MyButtonItem("18", "民生商品", R.drawable.icon_19));
        //  buttons.add(new MyButtonItem("19", "农副商品", R.drawable.icon_20));
        buttons.add(new MyButtonItem("20", "培训机构查询", R.drawable.icon_21));
        buttons.add(new MyButtonItem("21", "失物招领", R.drawable.icon_22));
        buttons.add(new MyButtonItem("22", "实时公交", R.drawable.icon_23));
        buttons.add(new MyButtonItem("23", "平安管家", R.drawable.icon_24));
        buttons.add(new MyButtonItem("24", "燃气", R.drawable.icon_25));
        buttons.add(new MyButtonItem("25", "人事人才档案", R.drawable.icon_26));
        buttons.add(new MyButtonItem("26", "三坊七巷", R.drawable.icon_27));
        buttons.add(new MyButtonItem("27", "社保", R.drawable.icon_28));
        buttons.add(new MyButtonItem("28", "社区服务", R.drawable.icon_29));
        buttons.add(new MyButtonItem("29", "社区网点", R.drawable.icon_30));
        buttons.add(new MyButtonItem("30", "市图书馆", R.drawable.icon_31));
        buttons.add(new MyButtonItem("31", "水费", R.drawable.icon_32));
        buttons.add(new MyButtonItem("32", "随手拍", R.drawable.icon_33));
        buttons.add(new MyButtonItem("33", "台风路径", R.drawable.icon_34));
        buttons.add(new MyButtonItem("34", "停车诱导", R.drawable.icon_35));
        buttons.add(new MyButtonItem("35", "网上办事大厅", R.drawable.icon_36));
        buttons.add(new MyButtonItem("36", "信用福州", R.drawable.icon_37));
        buttons.add(new MyButtonItem("37", "信用商家", R.drawable.icon_38));
        buttons.add(new MyButtonItem("38", "信用支付", R.drawable.icon_39));
        buttons.add(new MyButtonItem("39", "信用租赁", R.drawable.icon_40));
        buttons.add(new MyButtonItem("40", "药店支付", R.drawable.icon_41));
        buttons.add(new MyButtonItem("41", "医保", R.drawable.icon_42));
        buttons.add(new MyButtonItem("42", "医保药品目录", R.drawable.icon_43));
        buttons.add(new MyButtonItem("43", "医疗网点查询", R.drawable.icon_44));
        buttons.add(new MyButtonItem("44", "易办税", R.drawable.icon_45));
        buttons.add(new MyButtonItem("45", "意见反馈", R.drawable.icon_46));
        buttons.add(new MyButtonItem("46", "政民互动", R.drawable.icon_47));
        buttons.add(new MyButtonItem("47", "职业健康查询", R.drawable.icon_48));
        buttons.add(new MyButtonItem("48", "中考查询", R.drawable.icon_49));
        buttons.add(new MyButtonItem("49", "周边服务", R.drawable.icon_50));
        buttons.add(new MyButtonItem("50", "添加", R.drawable.icon_add));

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
        myAdapter = new MyAdapter(buttons);
        springboard.setAdapter(myAdapter);
        mIndicator.init();


        springboard.setOnItemClickListener(new SpringboardView.OnItemClickListener() {
            @Override
            public void onItemClick(FavoritesItem item) {
                MyButtonItem myItem = (MyButtonItem) item;
            }
        });

        springboard.setOnPageChangedListener(new SpringboardView.OnPageChangedListener() {
            @Override
            public void onPageScroll(int from, int to) {
                Log.d(" springboardview scroll from page#" + from + " to page#" + to);
                mIndicator.setCurrentPage(to);
            }

            @Override
            public void onPageCountChange(int oldCount, int newCount) {
                Log.d("springboardview page count has changed from #" + oldCount + " to #" + newCount);
                mIndicator.setPages(newCount);
                mIndicator.setCurrentPage(springboard.getmCurScreen());
            }
        });

        mIndicator.setPages(springboard.getPageCount());
        mIndicator.setCurrentPage(0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Object newP() {
        return null;
    }


}

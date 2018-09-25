package com.panxiaohe.springboard.demo.M;

import android.support.annotation.NonNull;

import com.panxiaohe.springboard.demo.bean.MyAppItem;
import com.panxiaohe.springboard.demo.bean.MyAppItem_;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.objectbox.query.QueryFilter;

/**
 * 作者：xin on 2018/9/21 10:29
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class ModelHome {

    MyAppBox mAppBox;

    /**
     * 数据库获取数据
     */
    public List<MyAppItem> getAppFromDB() {
        List<MyAppItem> items = getAppBox().getQueryBuilder().equal(MyAppItem_.isDisplay, true).
                order(MyAppItem_.sort).
                build().find();
        List<MyAppItem> folders = getAppBox().getQueryBuilder().filter(new QueryFilter<MyAppItem>() {
            @Override
            public boolean keep(@NonNull MyAppItem entity) {
                return entity.isFolder();
            }
        }).build().find();

        for (MyAppItem app : folders) {
            for (MyAppItem subApp : app.getSubApps()) {
                if (items.contains(subApp))
                    items.remove(subApp);
            }
        }
        for(MyAppItem it :items){
            if(it.isFolder()){
                //   排序
                Collections.sort(it.getSubApps(), new Comparator<MyAppItem>() {
                    @Override
                    public int compare(MyAppItem o1, MyAppItem o2) {
                        return o1.getFolderSort() - o2.getFolderSort();
                    }
                });
            }
        }

        return items;
    }


    public ArrayList<MyAppItem> getAppFromNet() {
        ArrayList<MyAppItem> myApps = new ArrayList<>();
        myApps.add(new MyAppItem("0", "24小时图书馆", "icon_1"));
        myApps.add(new MyAppItem("1", "12345", "icon_2"));
        myApps.add(new MyAppItem("2", "不动产状况", "icon_3"));
        myApps.add(new MyAppItem("3", "车主服务", "icon_4"));
        myApps.add(new MyAppItem("4", "城市停车", "icon_5"));
        myApps.add(new MyAppItem("5", "电动车目录", "icon_6"));
        myApps.add(new MyAppItem("6", "电梯查询", "icon_7"));
        myApps.add(new MyAppItem("7", "电影娱乐", "icon_8"));
        myApps.add(new MyAppItem("8", "公积金", "icon_9"));
        myApps.add(new MyAppItem("9", "公园景区", "icon_10"));
        myApps.add(new MyAppItem("10", "惠民资金", "icon_11"));
        myApps.add(new MyAppItem("11", "机动车维修点", "icon_12"));
        myApps.add(new MyAppItem("12", "健康证查询", "icon_13"));
        myApps.add(new MyAppItem("13", "交警服务", "icon_14"));
        myApps.add(new MyAppItem("14", "交通出行", "icon_15"));
        myApps.add(new MyAppItem("15", "教育缴费", "icon_16"));
        myApps.add(new MyAppItem("16", "景点门票", "icon_17"));
        myApps.add(new MyAppItem("17", "看病就医", "icon_18"));
        myApps.add(new MyAppItem("18", "民生商品", "icon_19"));
        //  myApps.add(new MyButtonItem("19", "农副商品", "icon_20"));
        myApps.add(new MyAppItem("20", "培训机构查询", "icon_21"));
        myApps.add(new MyAppItem("21", "失物招领", "icon_22"));
        myApps.add(new MyAppItem("22", "实时公交", "icon_23"));
        myApps.add(new MyAppItem("23", "平安管家", "icon_24"));
        myApps.add(new MyAppItem("24", "燃气", "icon_25"));
        myApps.add(new MyAppItem("25", "人事人才档案", "icon_26"));
        myApps.add(new MyAppItem("26", "三坊七巷", "icon_27"));
        myApps.add(new MyAppItem("27", "社保", "icon_28"));
        myApps.add(new MyAppItem("28", "社区服务", "icon_29"));
        myApps.add(new MyAppItem("29", "社区网点", "icon_30"));
        myApps.add(new MyAppItem("30", "市图书馆", "icon_31"));
        myApps.add(new MyAppItem("31", "水费", "icon_32"));
        myApps.add(new MyAppItem("32", "随手拍", "icon_33"));
        myApps.add(new MyAppItem("33", "台风路径", "icon_34"));
        myApps.add(new MyAppItem("34", "停车诱导", "icon_35"));
        myApps.add(new MyAppItem("35", "网上办事大厅", "icon_36"));
        myApps.add(new MyAppItem("36", "信用福州", "icon_37"));
        myApps.add(new MyAppItem("37", "信用商家", "icon_38"));
        myApps.add(new MyAppItem("38", "信用支付", "icon_39"));
        myApps.add(new MyAppItem("39", "信用租赁", "icon_40"));
        myApps.add(new MyAppItem("40", "药店支付", "icon_41"));
        myApps.add(new MyAppItem("41", "医保", "icon_42"));
        myApps.add(new MyAppItem("42", "医保药品目录", "icon_43"));
        myApps.add(new MyAppItem("43", "医疗网点查询", "icon_44"));
        myApps.add(new MyAppItem("44", "易办税", "icon_45"));
        myApps.add(new MyAppItem("45", "意见反馈", "icon_46"));
        myApps.add(new MyAppItem("46", "政民互动", "icon_47"));
        myApps.add(new MyAppItem("47", "职业健康查询", "icon_48"));
        myApps.add(new MyAppItem("48", "中考查询", "icon_49"));
        myApps.add(new MyAppItem("49", "周边服务", "icon_50"));
      //  myApps.add(new MyAppItem("50", "添加", "icon_add"));

        for (int i = 0; i < myApps.size(); i++) {
            myApps.get(i).setSort(i);
        }

        return myApps;
    }


    /**
     * 获取所有的app,不包括文件夹的
     */
    public List<MyAppItem> getAllAppsNotFileFromDb() {
        return getAppBox().getQueryBuilder().filter(new QueryFilter<MyAppItem>() {
            @Override
            public boolean keep(@NonNull MyAppItem entity) {
                return !entity.isFolder();
            }
        }).build().find();
    }


    /**
     * 获取所有的app,不包括文件夹的
     */
    public List<MyAppItem> getAllAppsFromDb() {
        return getAppBox().queryAll();
    }


    public MyAppBox getAppBox() {
        if (mAppBox == null) {
            mAppBox = new MyAppBox();
        }

        return mAppBox;
    }
}

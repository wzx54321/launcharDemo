package com.panxiaohe.springboard.demo;


import com.panxiaohe.springboard.demo.bean.MyObjectBox;

import xin.framework.app.App;
import xin.framework.store.box.utils.BoxStoreExtedUtils;

/**
 * 作者：xin on 2018/9/19 18:40
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class MyApp extends App {



    @Override
    public void onCreate() {
        super.onCreate();
        BoxStoreExtedUtils.initBoxStore(MyObjectBox.builder().androidContext(this).build());


    }
}

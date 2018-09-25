package com.panxiaohe.springboard.demo.M;

import com.panxiaohe.springboard.demo.bean.MyAppItem;
import com.panxiaohe.springboard.demo.bean.MyAppItem_;

import xin.framework.store.box.base.BaseBoxManager;
import xin.framework.store.box.utils.BoxStoreExtedUtils;

/**
 * 作者：xin on 2018/9/21 11:13
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class MyAppBox extends BaseBoxManager<MyAppItem> {
    public MyAppBox() {
        super(MyAppItem.class, BoxStoreExtedUtils.getBoxStore());
    }

    @Override
    public String getTableName() {
        return MyAppItem_.__DB_NAME;
    }
}

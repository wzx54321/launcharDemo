package com.panxiaohe.springboard.demo.entity;

import android.support.annotation.DrawableRes;

import com.panxiaohe.springboard.library.FavoritesItem;

import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;
import io.objectbox.relation.ToMany;
import xin.framework.utils.android.view.ResFinder;

/**
 * Created by panxiaohe on 16/3/20.
 * 继承
 */
@Entity
public class MyAppItem extends FavoritesItem {
    private static long serialVersionUID = 1L;

    @Id
    private long id;

    private String actionId;

    private String name;


    /**
     * 用来来标记是显示已有的状态
     */
    private boolean isDisplay;


    private int sort;

    /**
     * 文件夹中的排序
     */
    int folderSort = Integer.MAX_VALUE;

    @Transient
    private int icon;

    private String iconUrl;


    private String iconResName;

     @Backlink
     public ToMany<MyAppItem> subApps;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public void setActionId(String id) {
        this.actionId = id;
    }

    @Override
    public String getActionId() {
        return actionId;
    }


    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }


    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;

    }

    public @DrawableRes
    int getIcon() {

        if (icon == 0) {
            icon = ResFinder.findDrawableResByName(iconResName);
        }
        return icon;
    }


    public String getIconResName() {
        return iconResName;
    }

    public void setIconResName(String iconResName) {
        this.iconResName = iconResName;

        setIcon(ResFinder.findDrawableResByName(iconResName));
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setSubApps(ToMany<MyAppItem> subApps) {
        this.subApps = subApps;
    }


    public List<MyAppItem> getSubApps() {
        return subApps;
    }



    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }


    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public boolean isFolder() {
        return subApps != null && !subApps.isEmpty();
    }

    @Override
    public void addSubApp(FavoritesItem item, String defaultFolderName) {

        if (!isFolder()) {
            MyAppItem newItem = new MyAppItem();
            newItem.copy(this);

            setActionId("-1");
            setIconResName("");
            setName(defaultFolderName);
            subApps.add(newItem);

        }
        subApps.add((MyAppItem) item);// 当前的改变了
    }

    @Override
    protected void addSubItem(int position, FavoritesItem item) {
        subApps.add(position, (MyAppItem) item);
    }

    @Override
    protected MyAppItem removeSubItem(int position) {
        return subApps.remove(position);
    }

    @Override
    public void removeSubApp(int position) {
        subApps.remove(position);

        if (subApps.size() == 1) {
            copy(subApps.get(0));
            subApps = null;

        }


    }

    private void copy(MyAppItem myButtonItem) {
        this.name = myButtonItem.name;
        this.actionId = myButtonItem.actionId;
        this.icon = myButtonItem.icon;
        this.iconUrl = myButtonItem.iconUrl;//
        this.iconResName = myButtonItem.iconResName;//
        this.isDisplay = myButtonItem.isDisplay;//
        this.id = myButtonItem.id;
    }


    @Override
    public int getSubItemCount() {
        return subApps == null ? 0 : subApps.size();
    }

    @Override
    public MyAppItem getSubItem(int position) {
        if (subApps != null) {
            if (subApps.size() > position) {
                return subApps.get(position);
            }

        }
        throw new IllegalStateException("按钮列表是空");
    }


    public MyAppItem(String actionId, String actionName, String iconResName) {
        this.name = actionName;
        this.actionId = actionId;
        setIconResName(iconResName);
        setDisplay(true);
    }

    public MyAppItem() {
    }

    public int getFolderSort() {
        return folderSort;
    }

    public void setFolderSort(int folderSort) {
        this.folderSort = folderSort;
    }
}

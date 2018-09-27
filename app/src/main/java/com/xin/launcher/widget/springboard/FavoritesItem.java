package com.xin.launcher.widget.springboard;


import java.io.Serializable;

/**
 * item model
 *
 * @author panxiaohe
 */
public abstract class FavoritesItem implements Serializable {
   private static long serialVersionUID=1L;

    public abstract boolean isFolder();

    public abstract String getName();

    public abstract void setName(String name);

    public abstract void setActionId(String id);

    public abstract long getId() ;
    /**
     * 如果this是文件夹,返回"-1"
     * 否则请返回该按钮的唯一识别.
     */
    public abstract String getActionId();

    /**
     * 从文件夹中删除按钮
     */
    public abstract void removeSubApp(int position);

    public abstract void addSubApp(FavoritesItem item, String defaultFolderName);

    protected abstract void addSubItem(int position, FavoritesItem item);

    protected abstract FavoritesItem removeSubItem(int position);

    public abstract int getSubItemCount();

    public abstract FavoritesItem getSubItem(int position);



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FavoritesItem myAppItem = (FavoritesItem) o;

        if (getId() != myAppItem.getId()) return false;
        return getActionId() != null ? getActionId().equals(myAppItem.getActionId()) : myAppItem.getActionId() == null;
    }



    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getActionId() != null ? getActionId().hashCode() : 0);
        return result;
    }

}

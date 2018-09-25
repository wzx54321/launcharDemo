package com.panxiaohe.springboard.library;


import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by panxiaohe on 16/3/19.
 * 适配器.
 */
public abstract class SpringboardAdapter<T extends FavoritesItem> {
    private ArrayList<T> items;

    private DataChangeListener<T> dataCahngeListener;

    private boolean isEditing = false;

    private boolean isTouching = false;

    private SoftReference<MenuView> springboardView;

    private SoftReference<FolderView> folderView;

    public void setDataCahngeListener(DataChangeListener<T> dataCahngeListener) {
        this.dataCahngeListener = dataCahngeListener;
    }

    /**
     * 首个层级数量
     */
    public int getCount() {
        return items.size();
    }

    /**
     * 文件夹里按钮数量
     */
    public int getSubItemCount(int position) {
        return items.get(position).getSubItemCount();
    }

    /**
     * 首个层级item
     */
    public T getItem(int position) {
        return items.get(position);
    }

    /**
     * 首个层级item
     */
    public T getSubItem(int folderPosition, int position) {
        return (T) items.get(folderPosition).getSubItem(position);
    }

    /**
     * @return 只需返回所需的view, 所有设置请在configUI里完成
     */
    public abstract FrameLayout initItemView(int position, ViewGroup parent);

    public abstract void configItemView(int position, FrameLayout frameLayout);

    /**
     * @return 只需返回所需的view, 所有设置请在configUI里完成
     */
    public abstract FrameLayout initSubItemView(int folderPosition, int position, ViewGroup parent);

    public abstract void configSubItemView(int folderPosition, int position, FrameLayout frameLayout);


    public MenuView getSpringboardView() {
        return springboardView.get();
    }

    public void setSpringboardView(MenuView mSpringboardView) {
        this.springboardView = new SoftReference<>(mSpringboardView);
    }

    public FolderView getFolderView() {
        if (folderView != null) {
            return folderView.get();

        }
        return null;
    }

    public void setFolderView(FolderView folderView) {
        this.folderView = new SoftReference<>(folderView);
    }

    /**
     * @param position 位置
     * @return true 可以删除
     */
    public boolean ifCanDelete(int position) {
//        Log.e("SpringboardAdapter",getItem(position).toString());
        return !getItem(position).isFolder();
    }

    /**
     * @param position 位置
     * @return true 可以删除
     */
    public boolean ifCanDelete(int folderPosition, int position) {
        return !getSubItem(folderPosition, position).isFolder();
    }

    public boolean ifCanMerge(int fromPosition, int toPosition) {
        return !(getItem(fromPosition).isFolder() || (!getSpringboardView().ifCanMove(toPosition)));
    }


    public void setEditing(boolean isEditing) {
        if (this.isEditing != isEditing) {
//            Log.e("SpringboardAdapter", "编辑模式" + isEditing);
            this.isEditing = isEditing;
            FolderView folder;
            if (folderView != null && (folder = folderView.get()) != null) {
                int count = folder.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = folder.getChildAt(i);
                    folder.setEditingMode(i, child, isEditing, true);
                }
                if (isEditting()) {
                    folder.requestFocus();
                }
                int count1 = getSpringboardView().getChildCount();
                for (int i = 0; i < count1; i++) {
                    View child1 = getSpringboardView().getChildAt(i);
                    getSpringboardView().setEditingMode(i, child1, isEditing, false);
                }
            } else {
                if (isEditting()) {
                    getSpringboardView().requestFocus();
                }
                int count1 = getSpringboardView().getChildCount();
                for (int i = 0; i < count1; i++) {
                    View child1 = getSpringboardView().getChildAt(i);
                    getSpringboardView().setEditingMode(i, child1, isEditing, true);
                }
            }
        }
    }

    public void initFolderEditingMode() {
        if (isEditing) {
            FolderView folder;
            if (folderView != null && (folder = folderView.get()) != null) {
                int count = folder.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = folder.getChildAt(i);
                    folder.setEditingMode(i, child, isEditing, true);
                }
                if (isEditting()) {
                    folder.requestFocus();
                }
            }
        }
    }

    public boolean isEditting() {
        return isEditing;
    }

    public void removeFolder() {
        setFolderView(null);
    }

    public abstract void onDataChange();

    public ArrayList<T> getItems() {
        return items;
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }


    protected boolean isTouching() {
        return isTouching;
    }

    protected void setIsTouching(boolean isTouching) {
        this.isTouching = isTouching;
    }


// ============================数据处理========================


    // 交换两项
    public void exchangeItem(int fromPosition, int toPosition) {
//        Log.e("SpringboardAdapter","exchangeItem fromPosition = "+fromPosition+"  toPosition = "+toPosition);
        T item = items.remove(fromPosition);

        SpringboardView container = getSpringboardView();

        FrameLayout view = (FrameLayout) container.getChildAt(fromPosition);

        container.removeView(view);

        items.add(toPosition, item);


        dataChange();

        container.addView(view, toPosition);
        if (dataCahngeListener != null /*&& !isTouching*/) {
            dataCahngeListener.exchangeItem(new ArrayList<>(items));
        }

        getSpringboardView().setEditingMode(toPosition, view, isEditing, true);

    }


    // 删除
    public void deleteItem(int position) {
        T app = items.remove(position);

        onDataChange();
        if (dataCahngeListener != null && !isTouching) {
            dataCahngeListener.onDeleteItem(new ArrayList<>(items), app);
        }
        getSpringboardView().removeViewAt(position);
    }

    // 修改姓名
    public void changeFolderName(String name) {
        int folderPosition = getFolderView().getFolderPosition();
        T item = items.get(folderPosition);
        if (item.isFolder()) {
            T app = items.get(folderPosition);
            String oldName = app.getName();
            if (!oldName.equals(name)) {
                app.setName(name);

                onDataChange();
                if (dataCahngeListener != null && !isTouching) {
                    dataCahngeListener.changeFolderName(app);
                }

                configItemView(folderPosition, (FrameLayout) getSpringboardView().getChildAt(folderPosition));
            }
        }
    }

    // 添加
    public void addItem(int position, T item) {
//        Log.e("SpringboardAdapter","addItem position = "+position);

        items.add(position, item);

        dataChange();
        if (dataCahngeListener != null ) {
            dataCahngeListener.addItem(new ArrayList<>(items));
        }

        FrameLayout view = getSpringboardView().initItemView(position);
        getSpringboardView().configView(view);
        getSpringboardView().addView(view, position);

        getSpringboardView().setEditingMode(position, view, isEditing, true);

    }


    //   两项合并
    public void mergeItem(int fromPosition, int toPosition, String defaultFolderName) {
//        Log.e("SpringboardAdapter","mergeItem fromPosition = "+fromPosition+"  toPosition = "+toPosition);
        T fromItem = items.get(fromPosition);// 1

        T toItem = items.get(toPosition);// 2

        boolean needCreateId = !toItem.isFolder();

        toItem.addSubApp(fromItem, defaultFolderName);// 1 to 2

        FrameLayout view = (FrameLayout) getSpringboardView().getChildAt(toPosition);

        configItemView(toPosition, view);

        getSpringboardView().setEditingMode(toPosition, view, isEditing, true);

        items.remove(fromPosition);

        dataChange();

        if (dataCahngeListener != null && !isTouching) {
            dataCahngeListener.mergeItem(needCreateId, toItem, fromItem, items);
        }

        getSpringboardView().removeViewAt(fromPosition);
    }


    // 添加到文件夹
    public void addItemToFolder(int dragPosition, T dragOutItem, String defaultName) {
//        Log.e( "addItemToFolder position = "+dragPosition);
        T folder = items.get(dragPosition);
        boolean needCreateId = !folder.isFolder();

        folder.addSubApp(dragOutItem, defaultName);

        dataChange();

        if (dataCahngeListener != null && !isTouching) {
            dataCahngeListener.addItemToFolder(needCreateId, items, folder, dragOutItem);
        }

        configItemView(dragPosition, (FrameLayout) getSpringboardView().getChildAt(dragPosition));
    }


    //   文件中交换两项
    public void exChangeSubItem(int folderPosition, int fromPosition, int toPosition) {
//        Log.e("SpringboardAdapter","exChangeSubItem folderPosition = "+folderPosition +"  fromPosition = "+fromPosition+"  toPosition = "+toPosition);
        T folder = items.get(folderPosition);

        T item = (T) folder.removeSubItem(fromPosition);
//        (T)folder.getMenuList().remove(fromPosition);

        SpringboardView container = getFolderView();

        FrameLayout view = (FrameLayout) container.getChildAt(fromPosition);

        container.removeView(view);

        folder.addSubItem(toPosition, item);

        dataChange();

        if (dataCahngeListener != null  ) {
            dataCahngeListener.exChangeSubItem(folder);
        }

        container.addView(view, toPosition);

        configItemView(folderPosition, (FrameLayout) getSpringboardView().getChildAt(folderPosition));

        getFolderView().setEditingMode(toPosition, view, isEditing, true);

    }


    //    删除文件文件夹中的
    public void deleteItem(int folderPosition, int position) {
//        Log.e("SpringboardAdapter","deleteItem folderPosition = "+folderPosition+"  position = "+position);
        T folder = items.get(folderPosition);// 文件夹
        T itemBeRemove = (T) folder.getSubItem(position);
        folder.removeSubApp(position);

        onDataChange();

        if (dataCahngeListener != null && !isTouching) {
            dataCahngeListener.onDeleteFolderItem( folder , new ArrayList<>(items), itemBeRemove);
        }

        getFolderView().removeViewAt(position);

        FrameLayout view = (FrameLayout) getSpringboardView().getChildAt(folderPosition);

        configItemView(folderPosition, view);

        if (!folder.isFolder()) {
            getSpringboardView().setEditingMode(folderPosition, view, isEditing, true);
            getSpringboardView().removeFolder();
        } else {
            getSpringboardView().setEditingMode(folderPosition, view, isEditing, false);
        }
    }

    // 拖出文件夹仿到外面
    public T tempRemoveItem(int folderPosition, int position) {
        T folder = items.get(folderPosition);

        T item = (T) folder.getSubItem(position);

        folder.removeSubApp(position);

        FrameLayout view = (FrameLayout) getSpringboardView().getChildAt(folderPosition);

        configItemView(folderPosition, view);

        getSpringboardView().setEditingMode(folderPosition, view, isEditing, true);
        // 最后都到了交换的方法，不需要数据处理
        return item;
    }

    private void dataChange() {
        if (!isTouching) {
            onDataChange();
        }
    }


    public interface DataChangeListener<T> {

        /**
         * @param ts  总的
         * @param app 删除掉的APP
         */
        void onDeleteItem(ArrayList<T> ts, T app);

        /**
         * 交换位置
         */
        void exchangeItem(ArrayList<T> ts);

        /**
         * 修改文件夹的名称
         */
        void changeFolderName(T app);

        void addItem(ArrayList<T> ts);


        /**
         * 添加到文件夹
         */
        void addItemToFolder(boolean needCreateId, ArrayList<T> items, T folder, T app);

        void mergeItem(boolean needCreateId, T toItem, T fromItem, ArrayList<T> items);

        void exChangeSubItem(T folder);

        void onDeleteFolderItem(T folder, ArrayList<T> ts, T itemBeRemove);
    }
}
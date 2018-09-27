package com.xin.launcher.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xin.launcher.widget.springboard.SpringboardAdapter;
import com.xin.launcher.utils.ImageUtil;
import com.xin.launcher.bean.MyAppItem;

import java.util.ArrayList;

import xin.ui.launcher.R;


public class MyAdapter extends SpringboardAdapter<MyAppItem> {

    public MyAdapter(ArrayList<MyAppItem> items) {
        setItems(items);
    }

    @Override
    public FrameLayout initItemView(int position, ViewGroup parent) {
        return (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void configItemView(int position, FrameLayout view) {
        MyAppItem item = getItem(position);
        ImageView menu_icon = (ImageView) view.findViewById(R.id.menu_icon);
//        ImageView menu_redDot = (ImageView)view.findViewById(R.id.redDot);
        TextView menu_name = (TextView) view.findViewById(R.id.menu_name);
//        ImageView menu_del = (ImageView)view.findViewById(R.id.menu_del);
        final LinearLayout folder = (LinearLayout) view.findViewById(R.id.folder);
//        FrameLayout menu_container = (FrameLayout) view.findViewById(R.id.menu_container);

        menu_name.setText(item.getName());
        if (item.isFolder()) {
            menu_icon.setVisibility(View.GONE);
            folder.setVisibility(View.VISIBLE);
            ImageView[] images = new ImageView[9];
            images[0] = folder.findViewById(R.id.folder_button1);
            images[1] = folder.findViewById(R.id.folder_button2);
            images[2] = folder.findViewById(R.id.folder_button3);
            images[3] = folder.findViewById(R.id.folder_button4);
            images[4] = folder.findViewById(R.id.folder_button5);
            images[5] = folder.findViewById(R.id.folder_button6);
            images[6] = folder.findViewById(R.id.folder_button7);
            images[7] = folder.findViewById(R.id.folder_button8);
            images[8] = folder.findViewById(R.id.folder_button9);
            for (int i = 0; i < images.length; i++) {
                if (item.getSubItemCount() > i) {
                    MyAppItem button = item.getSubItem(i);
                    Drawable drawable = view.getResources().getDrawable(button.getIcon());
                    images[i].setImageDrawable(drawable);
                    images[i].setVisibility(View.VISIBLE);
                } else {
                    images[i].setVisibility(View.INVISIBLE);
                }
            }
        } else {
            menu_icon.setVisibility(View.VISIBLE);
            folder.setVisibility(View.GONE);

            StateListDrawable drawable = ImageUtil.getStateListDrawable(item.getIcon(), view.getContext());
            menu_icon.setImageDrawable(drawable);


        }
    }

    @Override
    public FrameLayout initSubItemView(int folderPosition, int position, ViewGroup parent) {
        return (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
    }

    @Override
    public void configSubItemView(int folderPosition, int position, FrameLayout view) {
        MyAppItem item = getSubItem(folderPosition, position);
        ImageView mIcon = (ImageView) view.findViewById(R.id.menu_icon);
//        ImageView menu_redDot = (ImageView)view.findViewById(R.id.redDot);
        TextView menu_name = (TextView) view.findViewById(R.id.menu_name);
//        ImageView menu_del = (ImageView)view.findViewById(R.id.menu_del);
        LinearLayout folder = (LinearLayout) view.findViewById(R.id.folder);
//        FrameLayout menu_container = (FrameLayout) view.findViewById(R.id.menu_container);
        menu_name.setText(item.getName());
        if (item.isFolder()) {
//            menu_container.setBackgroundResource(R.drawable.folder_icon);
            mIcon.setVisibility(View.GONE);
            folder.setVisibility(View.VISIBLE);
            ImageView[] images = new ImageView[4];
            images[0] = (ImageView) folder.findViewById(R.id.folder_button1);
            images[1] = (ImageView) folder.findViewById(R.id.folder_button2);
            images[2] = (ImageView) folder.findViewById(R.id.folder_button3);
            images[3] = (ImageView) folder.findViewById(R.id.folder_button4);
            for (int i = 0; i < 4; i++) {
                if (item.getSubItemCount() > i) {
                    MyAppItem button = item.getSubItem(i);
                    Drawable drawable = view.getResources().getDrawable(button.getIcon());
                    images[i].setImageDrawable(drawable);
                    images[i].setVisibility(View.VISIBLE);
                } else {
                    images[i].setVisibility(View.INVISIBLE);
                }
            }
        } else {
//            menu_container.setBackgroundResource(R.drawable.folder_icon);
//            menu_container.setBackgroundResource(0);
            mIcon.setVisibility(View.VISIBLE);
            folder.setVisibility(View.GONE);
            StateListDrawable drawable = ImageUtil.getStateListDrawable(item.getIcon(), view.getContext());
            mIcon.setImageDrawable(drawable);
        }
    }

    @Override
    public boolean ifCanMerge(int fromPosition, int toPosition) {
        return super.ifCanMerge(fromPosition, toPosition);
    }

    @Override
    public void onDataChange() {
        IS_DATACAHNGE = true;
    }

}

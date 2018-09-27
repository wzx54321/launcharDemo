package com.xin.launcher.widget.springboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import xin.framework.utils.android.ScreenUtils;
import xin.ui.launcher.R;

/**
 * 页面索引
 *
 */
public class DotView extends View {


    private Drawable dotActive;
    private Drawable dotDeactive;
    private int drawingBound;
    private int curPage;
    private int pagerLeft;
    private int pagerTop;
    private int pagerWidth;
    private int pages = 1;

    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public DotView(Context context, AttributeSet attr, int style) {
        super(context, attr, style);
    }

    public void init(  ) {

        Resources res = getResources();
        this.dotActive = res.getDrawable(R.drawable.dot_active);
        this.dotDeactive = res.getDrawable(R.drawable.dot_deactive);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //计算总宽度
        pagerWidth = (pages * dotActive.getIntrinsicWidth() + (pages - 1) * ScreenUtils.dp2px(8));
        //计算左间距
        pagerLeft = ((getWidth() - pagerWidth) / 2);
        //设置上间距
        pagerTop = ScreenUtils.dp2PxInt(1);
        //设置高度范围
        drawingBound = (pagerTop + dotActive.getIntrinsicHeight() + pagerTop);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int dotWidth = dotActive.getIntrinsicWidth();
        int dotHeight = dotActive.getIntrinsicHeight();
        int left = pagerLeft;
        for (int i = 0; i < pages; i++) {
            Drawable dotDrawable;
            if (curPage == i) {
                //当前页显示激活
                dotDrawable = dotActive;
            } else {
                dotDrawable = dotDeactive;
            }
            dotDrawable.setBounds(left, pagerTop, left + dotWidth, dotHeight + pagerTop);
            dotDrawable.draw(canvas);
            left += dotWidth +  ScreenUtils.dp2px(8);
        }
        super.onDraw(canvas);
    }

    /**
     * 设置当前页
     * @param curPage
     */
    public void setCurrentPage(int curPage) {
        this.curPage = curPage;
        invalidatePager();
    }

    /**
     * 设置总页数
     * @param pageCount
     */
    public void setPages(int pageCount) {
        this.pages = Math.max(1, pageCount);
        invalidatePager();
    }

    /**
     * 重绘
     */
    private void invalidatePager() {
        invalidate(0, 0, getWidth(), drawingBound);
    }
}

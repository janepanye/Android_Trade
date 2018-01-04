package com.coco3g.caopantx.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CompoundButton;


import com.coco3g.caopantx.R;

/**
 * Created by hhh on 2017/12/24.
 */

public class ImageTextButton extends CompoundButton {

    private int resourceId = 0;
    private Bitmap bitmap;

    public ImageTextButton(Context context) {
        super(context,null);
    }

    public ImageTextButton(Context context,AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setClickable(true);
        resourceId = R.drawable.abc_ic_ab_back_mtrl_am_alpha;
        bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
    }

    public void setIcon(int resourceId)
    {
        this.bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub

        // 图片顶部居中显示
        int x = (this.getMeasuredWidth() - bitmap.getWidth())/2;
        int y = 5;
        canvas.drawBitmap(bitmap, x, y, null);
        // 坐标需要转换，因为默认情况下Button中的文字居中显示
        // 这里需要让文字在底部显示
        canvas.translate(0,(this.getMeasuredHeight()/2) - (int) this.getTextSize());
        super.onDraw(canvas);
    }
}

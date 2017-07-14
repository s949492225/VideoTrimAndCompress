package com.syiyi.digger.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.syiyi.digger.R;


/**
 * Description: 实现范围滑杠选择器(RangeSeekBar)。 SeekBar上有两个滑块，一个选择最小值，一个选择最大值，从而选择了一个范围(range)。
 */
public class RangeSeekBar2 extends View {


    private Bitmap lowerBmp;
    private Bitmap upperBmp;
    private int bmpWidth;
    private int bmpHeight;
    private int lowerCenterX;
    private int upperCenterX;
    private int lineStart;
    private int lineEnd;

    public RangeSeekBar2(Context context) {
        super(context);
        init();
    }

    public RangeSeekBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RangeSeekBar2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        lowerBmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.video_trim_handle);
        upperBmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.video_trim_handle);


        bmpWidth = upperBmp.getWidth();
        bmpHeight = upperBmp.getHeight();

        lowerCenterX = lineStart;
        upperCenterX = lineEnd;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


}
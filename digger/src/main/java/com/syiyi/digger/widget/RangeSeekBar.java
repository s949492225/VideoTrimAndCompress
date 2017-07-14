package com.syiyi.digger.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.syiyi.digger.R;
import com.syiyi.digger.util.DeviceUtil;


/**
 * Description: 实现范围滑杠选择器(RangeSeekBar)。 SeekBar上有两个滑块，一个选择最小值，一个选择最大值，从而选择了一个范围(range)。
 */
public class RangeSeekBar extends View {


    private Bitmap lowerBmp;
    private Bitmap upperBmp;
    private int bmpWidth;
    private int lowerCenterX;
    private int upperCenterX;
    private Paint mPaint;
    private int backGroundColor;
    private int borderColor;
    private int lineHeight;
    private static final String COLOR = "#99000000";
    private static final String BORDER_COLOR = "#ffcc33";
    private static final int LINE_HEIGHT = 2;
    private boolean isLowerMoving;
    private boolean isUpperMoving;
    private OnRangeChangeListener mRangeChangeListner;

    public RangeSeekBar(Context context) {
        super(context);
        init();

    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        backGroundColor = Color.parseColor(COLOR);
        borderColor = Color.parseColor(BORDER_COLOR);
        lineHeight = DeviceUtil.dip2px(getContext(), LINE_HEIGHT);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        if (lowerCenterX == 0) {

            lowerBmp = resizeBitmap(R.drawable.video_trim_handle, getMeasuredHeight());
            upperBmp = resizeBitmap(R.drawable.video_trim_handle, getMeasuredHeight());

            bmpWidth = upperBmp.getWidth();

            lowerCenterX = bmpWidth / 2;
            upperCenterX = getMeasuredWidth() - bmpWidth / 2;
        }
    }

    public Bitmap resizeBitmap(int id, int h) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                id);
        if (bitmap != null) {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            float scaleHeight = ((float) h) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleHeight, scaleHeight);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        } else {
            return null;
        }
    }


    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getMeasuredHeight();
        //中间上下边框
        mPaint.setColor(borderColor);
        mPaint.setStrokeWidth(lineHeight);
        //上线
        canvas.drawLine(lowerCenterX, lineHeight / 2, upperCenterX, lineHeight / 2, mPaint);
        //下线
        int lineBottom = height - lineHeight / 2;
        canvas.drawLine(lowerCenterX, lineBottom, upperCenterX, lineBottom, mPaint);

        //left
        mPaint.setColor(backGroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        canvas.drawRect(0, 0, lowerCenterX, height, mPaint);
        //right
        canvas.drawRect(upperCenterX, 0, width, height, mPaint);

        //滑块
        mPaint.setColor(Color.WHITE);
        canvas.drawBitmap(lowerBmp, lowerCenterX - bmpWidth / 2, 0, mPaint);
        canvas.drawBitmap(upperBmp, upperCenterX - bmpWidth / 2, 0, mPaint);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        float xPos = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果按下的位置在垂直方向没有与图片接触，则不会滑动滑块
                float yPos = event.getY();
                if (Math.abs(yPos - height / 2) > height / 2) {
                    return false;
                }

                // 表示当前按下的滑块是左边的滑块
                if (Math.abs(xPos - lowerCenterX) <= bmpWidth / 2) {
                    isLowerMoving = true;
                }

                // //表示当前按下的滑块是右边的滑块
                if (Math.abs(xPos - upperCenterX) <= bmpWidth / 2) {
                    isUpperMoving = true;
                }

                // 单击左边滑块的左边线条时，左边滑块滑动到对应的位置
                if (xPos < lowerCenterX - bmpWidth / 2) {
                    lowerCenterX = (int) xPos;
                    updateRange();
                    postInvalidate();
                }

                // 单击右边滑块的右边线条时， 右边滑块滑动到对应的位置
                if (xPos > upperCenterX + bmpWidth / 2) {
                    upperCenterX = (int) xPos;
                    updateRange();
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 滑动左边滑块时
                if (isLowerMoving) {
                    if (xPos >= bmpWidth / 2 && xPos < upperCenterX - bmpWidth) {
                        lowerCenterX = (int) xPos;
                        updateRange();
                        postInvalidate();
                    }
                }

                // 滑动右边滑块时
                if (isUpperMoving) {
                    if (xPos <= width - bmpWidth / 2 && xPos > lowerCenterX + bmpWidth) {
                        upperCenterX = (int) xPos;
                        updateRange();
                        postInvalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                // 修改滑块的滑动状态为不再滑动
                isLowerMoving = false;
                isUpperMoving = false;
                if (lowerCenterX < bmpWidth / 2) {
                    lowerCenterX = bmpWidth / 2;
                }
                if (upperCenterX > width - bmpWidth / 2) {
                    upperCenterX = width - bmpWidth / 2;
                }
                updateRange();
                postInvalidate();
                break;
            default:
                break;
        }

        return true;
    }

    private void updateRange() {
        int sum = getMeasuredWidth() - bmpWidth * 2;
        float start = (lowerCenterX - bmpWidth / 2) * 1.0f / sum;
        float end = (upperCenterX - bmpWidth * 1.5f) * 1.0f / sum;
        float selectPercent = end - start;
        if (selectPercent < 0) {
            selectPercent = 0f;
        }
        if (mRangeChangeListner != null) {
            mRangeChangeListner.onChange(start, end, selectPercent);
        }
    }

    public void setRangeChangeListener(OnRangeChangeListener listener) {
        mRangeChangeListner = listener;
    }

    public interface OnRangeChangeListener {
        void onChange(float startPercent, float endPercent, float selectPercent);
    }

}
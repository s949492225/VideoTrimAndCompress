/*
 * MIT License
 *
 * Copyright (c) 2016 Knowledge, education for life.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.syiyi.digger.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.view.View;
import iknow.android.utils.thread.BackgroundExecutor;
import iknow.android.utils.thread.UiThreadExecutor;

public class TimeLineView extends View {

    private Uri mVideoUri;
    private int mHeightView;
    private int mWidth;
    private LongSparseArray<Bitmap> mBitmapList = null;

    public TimeLineView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minW, widthMeasureSpec, 1);

        final int minH = getPaddingBottom() + getPaddingTop() + getSuggestedMinimumHeight();
        int h = resolveSizeAndState(minH, heightMeasureSpec, 1);

        setMeasuredDimension(w, h);
        mWidth=getMeasuredWidth();
        mHeightView=getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(final int w, int h, final int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        if (w != oldW) {
            getBitmap(w);
        }
    }

    private void getBitmap(final int viewWidth) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("calculateThumbList", 0L, "") {
                                       @Override
                                       public void execute() {
                                           try {
                                               LongSparseArray<Bitmap> thumbnailList = new LongSparseArray<>();

                                               MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                               mediaMetadataRetriever.setDataSource(getContext(), mVideoUri);

                                               // Retrieve media data
                                               long videoLengthInMs = Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))* 1000;
                                               Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

                                               final int bitmapWidth= bitmap.getWidth();
                                               final int bitmapHeight= bitmap.getHeight();

                                               final int thumbHeight = mHeightView;
                                               int thumbWidth = (int) (mHeightView*1.0/bitmapHeight*bitmapWidth);

                                               int thumbsNum = (int) Math.ceil(((float) viewWidth) / thumbWidth);

                                               thumbWidth=mWidth/thumbsNum;

                                               final long interval = videoLengthInMs / thumbsNum;

                                               for (int i = 0; i < thumbsNum; ++i) {
                                                   if (i!=0) {
                                                       bitmap = mediaMetadataRetriever.getFrameAtTime(i * interval, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                                                   }
                                                   try {
                                                       if (i==thumbsNum-1){
                                                           bitmap = Bitmap.createScaledBitmap(bitmap, mWidth-thumbWidth*(thumbsNum-1), thumbHeight, false);
                                                       }else {
                                                           bitmap = Bitmap.createScaledBitmap(bitmap, thumbWidth, thumbHeight, false);
                                                       }
                                                   } catch (Exception e) {
                                                       e.printStackTrace();
                                                   }
                                                   thumbnailList.put(i, bitmap);
                                                   setTimeLineBitmap(thumbnailList);
                                               }

                                               mediaMetadataRetriever.release();


                                           } catch (final Throwable e) {
                                               Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                                           }
                                       }
                                   }
        );
    }

    private void setTimeLineBitmap(final LongSparseArray<Bitmap> thumbnailList) {
        UiThreadExecutor.runTask("updateTimeLine", new Runnable() {
                    @Override
                    public void run() {
                        mBitmapList = thumbnailList;
                        invalidate();
                    }
                }
                , 0L);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmapList != null) {
            canvas.save();
            int posX = 0;
            for (int i = 0; i < mBitmapList.size(); i++) {
                Bitmap bitmap = mBitmapList.get(i);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, posX, 0, null);
                    posX = posX + bitmap.getWidth();
                }
            }
        }
    }

    public void setVideo(@NonNull Uri data) {
        mVideoUri = data;
    }
}

package com.syiyi.digger.page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.syiyi.digger.R;
import com.syiyi.digger.consts.Constrains;
import com.syiyi.digger.decoration.SpacesItemDecoration;
import com.syiyi.digger.models.VideoInfo;
import com.syiyi.digger.util.MediaInfoUtilKt;

import java.util.ArrayList;

import iknow.android.utils.DeviceUtil;
import iknow.android.utils.callback.SingleCallback;

/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class VideoSelectActivity extends AppCompatActivity {
    private ArrayList<VideoInfo> mDatas = new ArrayList<>();
    private TextView mBtnNext;
    private VideoInfo mVideoInfo;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.video_select_layout);

        mDatas = MediaInfoUtilKt.getAllVideoFile(this);
        VideoGridViewAdapter videoGridViewAdapter = new VideoGridViewAdapter(this, mDatas);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(videoGridViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBtnNext = findViewById(R.id.next_step);
        btnSelected(false);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();
            }
        });

        videoGridViewAdapter.setItemClickCallback(new SingleCallback<Boolean, VideoInfo>() {
            @Override
            public void onSingleCallback(Boolean isSelected, VideoInfo video) {
                selectedItem(isSelected, video);
            }
        });
    }

    private void selectedItem(boolean selected, VideoInfo video) {
        if (video != null) {
            mVideoInfo = video;
            btnSelected(selected);
        }
    }

    private void btnSelected(boolean selected) {
        mBtnNext.setEnabled(selected);
        mBtnNext.setTextAppearance(VideoSelectActivity.this, selected ? R.style
                .blue_text_18_style : R.style.gray_text_18_style);
    }

    private void nextPage() {
        if (mVideoInfo == null) return;
        Intent intent = new Intent(this, VideoTrimActivity.class);
        intent.putExtra(Constrains.VIDEO_PATH, mVideoInfo.getVideoPath());
        intent.putExtra(Constrains.VIDEO_DURATION, mVideoInfo.calcDuration().getDuration());
        int[] whScale = calculateThumbScale();
        intent.putExtra(Constrains.VIDEO_WIDTH, whScale[0]);
        intent.putExtra(Constrains.VIDEO_HEIGHT, whScale[1]);
        this.startActivity(intent);
    }

    private int[] calculateThumbScale() {
        Bitmap bitmap = getThumbBitmap();
        int deviceWidth = DeviceUtil.getDeviceWidth();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        double scale = height / THUMB_HEIGHT;
        if (scale == 0) return new int[]{0, 0};
        int realWidth = (int) (width / scale);
        if (realWidth == 0) return new int[]{0, 0};

        int num = deviceWidth % realWidth;
        if (deviceWidth % realWidth != 0) {
            num = num + 1;
        }
        realWidth = deviceWidth / num;
        int realHeight = height * realWidth / width;
        return new int[]{realWidth, realHeight};
    }

    private final int THUMB_HEIGHT = 50;

    private Bitmap getThumbBitmap() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mVideoInfo.getVideoPath());
        return mediaMetadataRetriever.getFrameAtTime();
    }

    @Override
    protected void onDestroy() {
        mDatas.clear();
        super.onDestroy();
    }
}
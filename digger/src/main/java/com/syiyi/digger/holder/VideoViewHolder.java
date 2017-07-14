package com.syiyi.digger.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.syiyi.digger.R;
import com.syiyi.digger.models.VideoInfo;
import com.syiyi.digger.page.VideoGridViewAdapter;
import com.syiyi.digger.util.MediaInfoUtilKt;

import iknow.android.utils.DateUtil;
import iknow.android.utils.DeviceUtil;

/**
 * Created by mac on 2017/7/13.
 */

public class VideoViewHolder extends RecyclerView.ViewHolder {

    ImageView videoCover, selectIcon;
    View videoItemView, videoSelectPanel;
    TextView durationTv;

    public VideoViewHolder(View itemView) {
        super(itemView);
        videoItemView = itemView.findViewById(R.id.video_view);
        videoCover = itemView.findViewById(R.id.cover_image);
        durationTv = itemView.findViewById(R.id.video_duration);
        videoSelectPanel = itemView.findViewById(R.id.video_select_panel);
        selectIcon = itemView.findViewById(R.id.select);

        int size = DeviceUtil.getDeviceWidth() / 4;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoCover.getLayoutParams();
        params.width = size;
        params.height = size;
        videoCover.setLayoutParams(params);
    }

    public void renderUI(Context context, final int pos, final VideoInfo video, final VideoGridViewAdapter.OnClickListener listener) {
        if (video == null) return;
        selectIcon.setSelected(video.getSelected());
        durationTv.setText(DateUtil.convertSecondsToTime(video.getDuration() / 1000));
        Glide.with(context).load(MediaInfoUtilKt.getVideoFilePath(video.getVideoPath())).into(videoCover);
        videoSelectPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(pos);
                }
            }
        });
    }
}

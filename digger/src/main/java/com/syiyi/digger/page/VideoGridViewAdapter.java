package com.syiyi.digger.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.syiyi.digger.R;
import com.syiyi.digger.models.VideoInfo;
import com.syiyi.digger.util.MediaInfoUtilKt;

import java.util.ArrayList;

import iknow.android.utils.DateUtil;
import iknow.android.utils.DeviceUtil;
import iknow.android.utils.callback.SingleCallback;


/**
 * Author：J.Chou
 * Date：  2016.08.01 3:45 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class VideoGridViewAdapter extends RecyclerView.Adapter<VideoGridViewAdapter.VideoViewHolder> {

    private Context mContext;
    private ArrayList<VideoInfo> mDatas;
    private SingleCallback<Boolean, VideoInfo> mCallBack;
    private ArrayList<VideoInfo> mVideoSel = new ArrayList<>();
    private ArrayList<ImageView> mSelIconList = new ArrayList<>();

    VideoGridViewAdapter(Context context, ArrayList<VideoInfo> dataList) {
        this.mContext = context;
        this.mDatas = dataList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_select_gridview_item, null);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        if (mDatas == null) return;
        VideoInfo video = mDatas.get(position);
        holder.durationTv.setText(DateUtil.convertSecondsToTime(video.getDuration() / 1000));
        Glide.with(mContext).load(MediaInfoUtilKt.getVideoFilePath(video.getVideoPath())).into(holder.videoCover);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    void setItemClickCallback(final SingleCallback singleCallback) {
        this.mCallBack = singleCallback;
    }

    private boolean isSelected = false;

    class VideoViewHolder extends RecyclerView.ViewHolder {

        ImageView videoCover, selectIcon;
        View videoItemView, videoSelectPanel;
        TextView durationTv;

        VideoViewHolder(final View itemView) {
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
            videoSelectPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDatas == null) return;
                    VideoInfo videoInfo = mDatas.get(getAdapterPosition());
                    if (mVideoSel.size() > 0) {
                        if (videoInfo.equals(mVideoSel.get(0))) {
                            selectIcon.setImageResource(R.drawable.icon_video_unselected);
                            clearAll();
                            isSelected = false;
                        } else {
                            mSelIconList.get(0).setImageResource(R.drawable.icon_video_unselected);
                            clearAll();
                            addData(videoInfo);
                            selectIcon.setImageResource(R.drawable.icon_video_selected);
                            isSelected = true;
                        }

                    } else {
                        clearAll();
                        addData(videoInfo);
                        selectIcon.setImageResource(R.drawable.icon_video_selected);
                        isSelected = true;
                    }
                    mCallBack.onSingleCallback(isSelected, mDatas.get(getAdapterPosition()));
                }
            });
        }

        private void addData(VideoInfo videoInfo) {
            mVideoSel.add(videoInfo);
            mSelIconList.add(selectIcon);
        }
    }

    private void clearAll() {
        mVideoSel.clear();
        mSelIconList.clear();
    }
}

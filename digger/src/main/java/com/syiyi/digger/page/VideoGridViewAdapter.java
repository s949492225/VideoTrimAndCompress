package com.syiyi.digger.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syiyi.digger.R;
import com.syiyi.digger.holder.VideoViewHolder;
import com.syiyi.digger.models.VideoInfo;

import java.util.List;


/**
 * Author：J.Chou
 * Date：  2016.08.01 3:45 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class VideoGridViewAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    private Context mContext;
    private OnClickListener mListener;
    private List<VideoInfo> mDatas;

    VideoGridViewAdapter(Context context, List<VideoInfo> dataList) {
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
        holder.renderUI(mContext, position, video, mListener);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
    }

    public interface OnClickListener {
        void onClick(int pos);
    }
}

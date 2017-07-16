package com.syiyi.digger.page;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.syiyi.digger.R;
import com.syiyi.digger.consts.Constrains;
import com.syiyi.digger.decoration.SpacesItemDecoration;
import com.syiyi.digger.ex.FileEx;
import com.syiyi.digger.init.Digger;
import com.syiyi.digger.models.VideoInfo;
import com.syiyi.digger.util.MediaInfoUtilKt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class VideoSelectActivity extends AppCompatActivity {
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private List<VideoInfo> mDatas = new ArrayList<>();
    private TextView mBtnNext;
    private VideoInfo mVideoInfo;
    private VideoGridViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.video_select_layout);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBtnNext = findViewById(R.id.next_step);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();
            }
        });
        btnSelected(false);

        mAdapter = new VideoGridViewAdapter(this, mDatas);
        mAdapter.setOnClickListener(new VideoGridViewAdapter.OnClickListener() {
            @Override
            public void onClick(int pos) {
                refreshData(pos);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        requestPermission();
    }

    private void requestData() {
        FileEx.createDir(new File(Digger.mOutputPath));
        mDatas.clear();
        mDatas.addAll(MediaInfoUtilKt.getAllVideoFile(this));
        mAdapter.notifyDataSetChanged();
    }

    private void refreshData(int pos) {
        if (mDatas == null) return;
        for (int i = 0; i < mDatas.size(); i++) {
            VideoInfo info = mDatas.get(i);
            if (pos == i) {
                if (!info.getSelected()) {
                    info.setSelected(true);
                    mVideoInfo = info;
                    btnSelected(true);
                }
            } else {
                info.setSelected(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, "请求存储权限", REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            requestData();
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("权限");
            builder.setMessage(rationale);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(VideoSelectActivity.this, new String[]{permission}, requestCode);
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestData();
                } else {
                    Toast.makeText(this, "存储权限授权失败,请手动设置!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mDatas != null) {
            mDatas.clear();
        }
        super.onDestroy();
    }
}
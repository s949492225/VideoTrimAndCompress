package com.syiyi.digger.page;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    private VideoInfo mVideoInfo;
    private VideoGridViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.video_select_layout);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("选择视频");
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setElevation(25);
        }

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

    private void nextPage() {
        if (mVideoInfo == null) {
            Toast.makeText(this, "请先选择视频", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, VideoTrimActivity.class);
        intent.putExtra(Constrains.VIDEO_PATH, mVideoInfo.getVideoPath());
        this.startActivityForResult(intent, Constrains.REQUEST_CODE_EDIT_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RESULT_OK)
            return;
        String path = data.getStringExtra(Constrains.VIDEO_PATH);
        Intent intent = new Intent();
        intent.putExtra(Constrains.VIDEO_PATH, path);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.done) {
            nextPage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (mDatas != null) {
            mDatas.clear();
        }
        super.onDestroy();
    }
}
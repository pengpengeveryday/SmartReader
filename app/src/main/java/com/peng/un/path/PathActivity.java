package com.peng.un.path;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.peng.un.R;
import com.peng.un.action.PathExplore;
import com.peng.un.data.Data;
import com.peng.un.utils.ALog;

import java.util.List;

public class PathActivity extends AppCompatActivity {
  private static final int PERMISSION_REQUEST_CODE = 1;
  private static final int MANAGE_STORAGE_REQUEST_CODE = 2;
  private RecyclerView recyclerView;
  private PathAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_path);
    ALog.d("PathActivity onCreate");

    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // 创建 PathAdapter 实例时传入 this 引用
    adapter = new PathAdapter(this);
    recyclerView.setAdapter(adapter);

    // 检查权限并浏览文件
    checkPermissionAndBrowse();
  }

  /**
   * 检查存储权限并进行相应操作
   */
  private void checkPermissionAndBrowse() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      // Android 11 及以上需要 MANAGE_EXTERNAL_STORAGE 权限
      if (!Environment.isExternalStorageManager()) {
        try {
          Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
          intent.setData(Uri.parse("package:" + getPackageName()));
          startActivityForResult(intent, MANAGE_STORAGE_REQUEST_CODE);
        } catch (Exception e) {
          Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
          startActivityForResult(intent, MANAGE_STORAGE_REQUEST_CODE);
        }
      } else {
        initializeFileList();
      }
    } else {
      // Android 10 及以下使用传统存储权限
      if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE},
          PERMISSION_REQUEST_CODE);
      } else {
        initializeFileList();
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == MANAGE_STORAGE_REQUEST_CODE) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Environment.isExternalStorageManager()) {
          initializeFileList();
        } else {
          Toast.makeText(this, "需要存储权限才能浏览文件", Toast.LENGTH_SHORT).show();
          finish();
        }
      }
    }
  }

  /**
   * 初始化文件列表
   */
  private void initializeFileList() {
    try {
      // 确保 PathExplore 已初始化
      PathExplore.instance();
      updateFileList();
    } catch (Exception e) {
      ALog.e("PathActivity: Error initializing file list: " + e.getMessage());
      Toast.makeText(this, "初始化文件列表失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // 权限授予，初始化文件列表
        initializeFileList();
      } else {
        // 权限拒绝，提示并关闭活动
        Toast.makeText(this, "需要存储权限才能浏览文件", Toast.LENGTH_SHORT).show();
        finish();
      }
    }
  }

  /**
   * 更新文件列表
   */
  protected void updateFileList() {
    try {
      // 获取当前文件夹的名称
      String pathName = PathExplore.instance().getPathName();
      // 更新 Activity 的标题为当前文件夹的名称
      setTitle(pathName);

      // 获取文件列表
      List<Data.File> dataList = PathExplore.instance().listFiles();
      ALog.d("PathActivity: Got " + dataList.size() + " files");
      
      // 更新适配器数据
      adapter.setData(dataList);
    } catch (Exception e) {
      // 处理异常，显示错误信息
      ALog.e("PathActivity: Error updating file list: " + e.getMessage());
      Toast.makeText(this, "获取文件列表失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }
}

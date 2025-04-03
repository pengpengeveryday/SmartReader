package com.peng.un.path;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
  private RecyclerView recyclerView;
  private PathAdapter adapter;
  private TextView tvCurrentPath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_path);
    ALog.d("PathActivity onCreate");

    // 初始化视图组件
    tvCurrentPath = findViewById(R.id.tvCurrentPath);
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // 初始化适配器
    adapter = new PathAdapter();
    recyclerView.setAdapter(adapter);

    // 检查权限并浏览文件
    checkPermissionAndBrowse();
  }

  /**
   * 检查存储权限并进行相应操作
   */
  private void checkPermissionAndBrowse() {
    if (ContextCompat.checkSelfPermission(this,
      Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      // 请求存储权限
      ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE},
        PERMISSION_REQUEST_CODE);
    } else {
      // 已有权限，初始化文件列表
      initializeFileList();
    }
  }

  /**
   * 初始化文件列表
   */
  private void initializeFileList() {
    updateFileList();
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
  private void updateFileList() {
    try {
      // 更新当前路径显示
      tvCurrentPath.setText(PathExplore.instance().getCurrentFolder().getPath());
      // 获取文件列表
      List<Data.File> dataList = PathExplore.instance().listFiles();
      // 更新适配器数据
      adapter.setData(dataList);
    } catch (Exception e) {
      // 处理异常，显示错误信息
      Toast.makeText(this, "获取文件列表失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }
}

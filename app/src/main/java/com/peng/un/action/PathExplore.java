package com.peng.un.action;

import com.peng.un.data.Data;
import com.peng.un.data.EPub;
import com.peng.un.data.Settings;
import com.peng.un.utils.ALog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PathExplore {
  // 单例实例
  private static PathExplore instance;
  private Data.File currentFolder;
  // 新增 currentFile 变量
  private Data.File currentFile;

  // 私有构造函数
  private PathExplore() {
    String initialPath = Settings.instance().getLastPath();
    java.io.File path = new java.io.File(initialPath);
    currentFolder = new Data.File(path.getName(), path.getAbsolutePath(), true);
    // 初始化 currentFile 为 null
    currentFile = null;
  }

  // 静态方法获取单例实例
  public static PathExplore instance() {
    if (instance == null) {
      instance = new PathExplore();
    }
    return instance;
  }

  public List<Data.File> listFiles() {
    java.io.File currentFile = new java.io.File(currentFolder.getPath());
    ALog.d("PathExplore: Listing files in path: " + currentFile.getAbsolutePath());
    ALog.d("PathExplore: Path exists: " + currentFile.exists());
    ALog.d("PathExplore: Path is directory: " + currentFile.isDirectory());
    ALog.d("PathExplore: Path can read: " + currentFile.canRead());
    
    java.io.File[] files = currentFile.listFiles();
    ALog.d("PathExplore: Number of files found: " + (files != null ? files.length : "null"));
    
    List<java.io.File> sortedFiles = new ArrayList<>();
    
    if (files != null) {
      sortedFiles.addAll(Arrays.asList(files));
      Collections.sort(sortedFiles, (f1, f2) -> {
        if (f1.isDirectory() && !f2.isDirectory()) {
          return -1;
        }
        if (!f1.isDirectory() && f2.isDirectory()) {
          return 1;
        }
        return f1.getName().compareToIgnoreCase(f2.getName());
      });
    } else {
      ALog.e("PathExplore: listFiles() returned null for path: " + currentFolder.getPath());
    }

    List<Data.File> dataList = new ArrayList<>();
    for (java.io.File file : sortedFiles) {
      if (file.getName().toLowerCase().endsWith(".epub")) {
        // 如果是 .epub 文件，构造 EPub 类型的数据
        dataList.add(new EPub(file.getName(), file.getAbsolutePath()));
      } else {
        // 其他文件，构造 Data.File 类型的数据
        dataList.add(new Data.File(file.getName(), file.getAbsolutePath(), file.isDirectory()));
      }
    }

    ALog.d("PathExplore: Returning " + dataList.size() + " items");
    return dataList;
  }

  public boolean navigateTo(java.io.File path) {
    if (path == null || !path.exists() || !path.isDirectory()) {
      ALog.e("PathExplore: Cannot navigate to path: " + (path != null ? path.getAbsolutePath() : "null"));
      return false;
    }
    currentFolder = new Data.File(path.getName(), path.getAbsolutePath(), true);
    // 导航成功后保存当前路径
    Settings.instance(null).saveLastPath(path.getAbsolutePath());
    ALog.d("PathExplore: Navigated to: " + path.getAbsolutePath());
    return true;
  }

  public void selectFile(Data.File file) {
    this.currentFile = file;
  }

  public Data.File getSelectFile() {
    return this.currentFile;
  }

  public Data.File getCurrentFolder() {
    return currentFolder;
  }

  public boolean isRootPath() {
    return new java.io.File(currentFolder.getPath()).getParent() == null;
  }

  public static boolean isParentDirectory(java.io.File file) {
    return file.getName().equals("..");
  }

  /**
   * 获取当前文件夹的名称
   *
   * @return 当前文件夹的名称
   */
  public String getPathName() {
    return currentFolder.getName();
  }

  public boolean goBack() {
    java.io.File currentFile = new java.io.File(currentFolder.getPath());
    java.io.File parentFile = currentFile.getParentFile();
    if (parentFile != null && parentFile.exists() && parentFile.isDirectory()) {
      currentFolder = new Data.File(parentFile.getName(), parentFile.getAbsolutePath(), true);
      // 导航成功后保存当前路径
      Settings.instance(null).saveLastPath(parentFile.getAbsolutePath());
      ALog.d("PathExplore: Navigated back to: " + parentFile.getAbsolutePath());
      return true;
    }
    ALog.e("PathExplore: Cannot go back from path: " + currentFolder.getPath());
    return false;
  }
}

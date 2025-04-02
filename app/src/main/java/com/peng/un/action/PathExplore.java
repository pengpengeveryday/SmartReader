package com.peng.un.action;

import com.peng.un.data.Data;
import com.peng.un.data.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PathExplore {
    // 单例实例
    private static PathExplore instance;
    private Data.File currentFolder;

    // 私有构造函数
    private PathExplore() {
        String initialPath = "/sdcard";//Settings.instance().getLastPath();
        java.io.File path = new java.io.File(initialPath);
        currentFolder = new Data.File(path.getName(), path.getAbsolutePath(), true);
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
        java.io.File[] files = currentFile.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }

        List<java.io.File> sortedFiles = new ArrayList<>(Arrays.asList(files));
        Collections.sort(sortedFiles, (f1, f2) -> {
            if (f1.isDirectory() && !f2.isDirectory()) {
                return -1;
            }
            if (!f1.isDirectory() && f2.isDirectory()) {
                return 1;
            }
            return f1.getName().compareToIgnoreCase(f2.getName());
        });

        if (!isRootPath()) {
            sortedFiles.add(0, currentFile.getParentFile());
        }

        List<Data.File> dataList = new ArrayList<>();
        for (java.io.File file : sortedFiles) {
            dataList.add(new Data.File(file.getName(), file.getAbsolutePath(), file.isDirectory()));
        }

        return dataList;
    }

    public boolean navigateTo(java.io.File path) {
        if (path == null || !path.exists() || !path.isDirectory()) {
            return false;
        }
        currentFolder = new Data.File(path.getName(), path.getAbsolutePath(), true);
        // 导航成功后保存当前路径
        Settings.instance(null).saveLastPath(path.getAbsolutePath());
        return true;
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
}

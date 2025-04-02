package com.peng.un.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PathExplore {
    private File currentPath;

    public PathExplore(String initialPath) {
        currentPath = new File(initialPath);
        if (!currentPath.exists() || !currentPath.isDirectory()) {
            currentPath = new File("/");
        }
    }

    public List<File> listFiles() {
        File[] files = currentPath.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }

        List<File> sortedFiles = new ArrayList<>(Arrays.asList(files));
        // 首先按照是否是目录排序，目录在前
        // 然后按照名称排序
        Collections.sort(sortedFiles, (f1, f2) -> {
            if (f1.isDirectory() && !f2.isDirectory()) {
                return -1;
            }
            if (!f1.isDirectory() && f2.isDirectory()) {
                return 1;
            }
            return f1.getName().compareToIgnoreCase(f2.getName());
        });

        // 如果不是根目录，添加返回上级目录的选项
        if (!isRootPath()) {
            sortedFiles.add(0, currentPath.getParentFile());
        }

        return sortedFiles;
    }

    public boolean navigateTo(File path) {
        if (path == null || !path.exists() || !path.isDirectory()) {
            return false;
        }
        currentPath = path;
        return true;
    }

    public File getCurrentPath() {
        return currentPath;
    }

    public boolean isRootPath() {
        return currentPath.getParent() == null;
    }

    public static boolean isParentDirectory(File file) {
        return file.getName().equals("..");
    }
} 
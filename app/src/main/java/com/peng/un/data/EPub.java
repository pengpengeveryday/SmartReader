package com.peng.un.data;

// 让 EPub 类继承自 Data 类
public class EPub extends Data.File {
    public EPub(String name, String path) {
        super(Type.EPUB, name, path, false);
    }
}

package com.peng.un.data;

public class Data {
    public enum Type {
        FILE
    }

    private Type type;

    public Data(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static class File extends Data {
        private String name;
        private String path;
        private boolean folder; // 新增变量

        public File(String name, String path, boolean folder) {
            super(Type.FILE);
            this.name = name;
            this.path = path;
            this.folder = folder; // 构造函数中初始化
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isFolder() {
            return folder;
        }

        public void setFolder(boolean folder) {
            this.folder = folder;
        }
    }
}

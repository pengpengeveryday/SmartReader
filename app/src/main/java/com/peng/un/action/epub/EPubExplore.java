package com.peng.un.action.epub;

import com.peng.un.data.EPub;

public class EPubExplore {

    private final static EPubExplore instance = new EPubExplore();

    public static EPubExplore instance() {
        return instance;
    }


    private EPub epub;

    private EPubExplore() {

    }

    public void load(EPub epub) {
        this.epub = epub;
    }
}

package com.peng.un.action.epub;

import com.peng.un.action.ZipFileExplore;
import com.peng.un.action.epub.data.ContentAccessItem;
import com.peng.un.action.epub.parse.ParseMainEntries;
import com.peng.un.action.epub.parse.ParseRefEntries;
import com.peng.un.data.EPub;
import com.peng.un.utils.ALog;

import java.util.ArrayList;

public class EPubExplore {

  private final static EPubExplore instance = new EPubExplore();

  public static EPubExplore instance() {
    return instance;
  }


  private EPub epub;
  private ZipFileExplore provider;
  private final ArrayList<ContentAccessItem> chapters = new ArrayList<>();
  private String basePath; // 文件路径的基础路径

  private ContentAccessItem selectedFoldableChapter;

  private EPubExplore() {

  }
  public void close() {
    if (provider != null) {
      provider.close();
    }
    chapters.clear();
  }

  public ArrayList<ContentAccessItem> chapters() {
    // step1，收集depth=0的章节
    ArrayList<ContentAccessItem> folderableChapters = new ArrayList<>();
    for (ContentAccessItem chapter : chapters) {
      if (chapter.depth == 0) {
        folderableChapters.add(chapter);
      } else {
        folderableChapters.get(folderableChapters.size() - 1).subItems().add(chapter);
      }
    }
    // step2，针对depth=0的章节，如果当前选中的foldable的章节为该章节，那么设置fold = true
    if (folderableChapters.size() > 0) {
      if (selectedFoldableChapter == null) {
        selectedFoldableChapter = folderableChapters.get(0);
      }
      for (ContentAccessItem chapter : folderableChapters) {
        if (selectedFoldableChapter == chapter && chapter.subItems().size() > 0) {
          chapter.fold = false;
        } else {
          chapter.fold = true;
        }
      }
    }
    // step3，收集需要展示的章节
    ArrayList<ContentAccessItem> toDisplay = new ArrayList<>();
    for (ContentAccessItem chapter : folderableChapters) {
      toDisplay.add(chapter);
      if (!chapter.fold) {
        toDisplay.addAll(chapter.subItems());
      }
    }
    return toDisplay;
  }

  public void load(EPub epub) {
    close();
    this.epub = epub;
    provider = new ZipFileExplore(epub.getPath());
    String[] tab = provider.epubContentList();
    String contentList = tab != null ? tab[1] : "";
    basePath = "";
    if (tab != null && tab[0].length() > 0) {
      int lastIndex = tab[0].lastIndexOf("/");
      if (lastIndex > 0) {
        basePath = tab[0].substring(0, lastIndex);
        if (basePath.length() > 0) {
          basePath += "/";
        }
      }
    }

    // step1: 解析入口目录
    new ParseMainEntries().parse(chapters, contentList);
    // step2: 如果主目录太少而zip文件夹中的文件数非常多，那么读取主目录链接的文件中解析目录项
    if (chapters.size() > 0 && chapters.size() < 10 && provider.entryNames().size() > 200) {
      for (ContentAccessItem entry : chapters) {
        String filename = basePath + entry.getLinkSrc();
        contentList = provider.read(filename);
        ArrayList<ContentAccessItem> newEntries = new ArrayList<>();
        new ParseRefEntries().parseRefEntries(newEntries, contentList);
        for (ContentAccessItem i : newEntries) {
          i.depth = entry.depth + 1;
        }
        entry.subItems().addAll(newEntries);
      }
    }
    // step3: 如果主目录为空，那么尝试从contentList解析出目录
    if (chapters.size() == 0) {
      new ParseRefEntries().parseRefEntries(chapters, contentList);
    }
    for (ContentAccessItem chapter : chapters) {
      ALog.d("EPubExplore> chapter: " + chapter.text.trim());
    }
  }
}

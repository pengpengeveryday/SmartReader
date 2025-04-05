package com.peng.un.action.epub.parse;

import com.peng.un.action.epub.data.ContentAccessItem;
import com.peng.un.utils.ALog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ParseMainEntries {

  public void parse(List<ContentAccessItem> chapters, String contentList) {
    chapters.clear();
    // step1: 解析入口目录
    if (contentList.length() > 0) {
      Document document = Jsoup.parse(contentList);
      Elements divs = document.select("navMap");
      boolean detectEntry = false;
      if (divs != null && divs.size() > 0) {
        divs = divs.get(0).select("navPoint");
        detectEntry = divs.size() > 0;
      }
      if (detectEntry) {
        addNavPoints(chapters, divs, 0);
      }
    }
    // step2: 删除重复的目录项
    ArrayList<String> uniqueKey = new ArrayList<>();
    ArrayList<ContentAccessItem> toDelete = new ArrayList<>();
    for (ContentAccessItem chapter : chapters) {
      String src = chapter.getLinkSrc();
      // 删除src为空的目录项
      if (src.length() == 0) {
        toDelete.add(chapter); continue;
      }
      // 删除src重复项
      if (uniqueKey.contains(src)) {
        toDelete.add(chapter); continue;
      }
      uniqueKey.add(src);
    }
    for (ContentAccessItem item : toDelete) {
      chapters.remove(item);
    }
  }

  private void addNavPoints(List<ContentAccessItem> chapters, Elements divs, int depth) {
    for (int i = 0; i < divs.size(); i++) {
      ContentAccessItem item = new ContentAccessItem(divs.get(i), depth);
      if (item.hasContent() && item.text().length() > 0) {
        chapters.add(item);
      }
      addNavPoints(chapters, item.children(), depth + 1);
    }
  }
}

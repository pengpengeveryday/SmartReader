package com.peng.un.action.epub.parse;

import com.peng.un.action.epub.data.ContentAccessItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ParseRefEntries {

  // 从contentList中解析目录项
  public void parseRefEntries(ArrayList<ContentAccessItem> chapters, String contentList) {
    if (contentList.length() > 0) {
      Document document = Jsoup.parse(contentList);
      Elements divs = document.select("a");
      if (divs != null && divs.size() > 0) {
        for (Element element : divs) {
          ContentAccessItem item = new ContentAccessItem(element, 0);
          chapters.add(item);
        }
      }
    }
    ArrayList<String> uniqueKey = new ArrayList<>();
    ArrayList<ContentAccessItem> toDelete = new ArrayList<>();
    for (ContentAccessItem chapter : chapters) {
      String src = chapter.getLinkSrc();
      if (src.length() == 0) {
        toDelete.add(chapter); continue;
      }
      if (uniqueKey.contains(src)) {
        toDelete.add(chapter); continue;
      }
      uniqueKey.add(src);
    }
    for (ContentAccessItem item : toDelete) {
      chapters.remove(item);
    }
  }
}

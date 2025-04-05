package com.peng.un.action.epub.data;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ContentAccessItem {
  public int depth;
  public String text = "";
  public final Element element;
  private final Elements children;
  public Element content = null;
  private ArrayList<ContentAccessItem> subItems = new ArrayList<>();
  public boolean fold = false;
  public ContentAccessItem copy() {
    return new ContentAccessItem(element, depth);
  }
  public ContentAccessItem(Element e, int d) {
    element = e; depth = d;
    children = element.children();
    for (int i = 0; i < children.size(); i++) {
      Element item = children.get(i);
      String nodeName = item.nodeName();
      if (nodeName.equals("content")) {
        content = item;
      }
    }
    Elements subs = element.select("text");
    if (subs.size() > 0) { text = subs.get(0).text(); }
    else { text = element.text(); }
  }
  public boolean hasContent() { return content != null; }
  public ArrayList<ContentAccessItem>subItems() { return subItems; }
  public Elements children() {
    return children;
  }

  public String getLinkSrc() {
    String src = null;
    if (content != null) {
      src = content.attr("src");
    }
    if (src == null || src.length() <= 0) {
      src = element.attr("href");
    }
    if (src != null && src.length() > 0) {
      int i = src.indexOf("#");
      if (i > 0) {
        return src.substring(0, i);
      }
      return src;
    }
    return "";
  }
}

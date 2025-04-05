package com.peng.un.epub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.peng.un.R;
import com.peng.un.action.epub.EPubExplore;
import com.peng.un.action.epub.data.ContentAccessItem;
import java.util.List;

// 定义 EnumChaptersAdapter 类，继承自 ListAdapter
public class EnumChaptersAdapter extends BaseAdapter implements ListView.OnItemClickListener, AdapterView.OnItemLongClickListener {
  private final EnumChaptersActivity owner;

  final List<ContentAccessItem> chapters = EPubExplore.instance().chapters();
  public EnumChaptersAdapter(EnumChaptersActivity owner) {
    this.owner = owner;
  }

  void updateData() {
    chapters.clear();
    chapters.addAll(EPubExplore.instance().chapters());
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return chapters.size();
  }

  @Override
  public Object getItem(int i) {
    return chapters.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    if (view == null) {
      view = LayoutInflater.from(owner).inflate(R.layout.item_chapter, null);
    }
    // 获取当前位置的章节名称
    ContentAccessItem chapter = (ContentAccessItem) getItem(i);
    // 设置章节名称到 TextView
    String indention = "";
    int depth = chapter.depth;
    if (depth > 0) {
      indention += "└";
    }
    while (depth > 0) {
      indention = "  " + indention; depth--;
    }
    TextView tv = view.findViewById(R.id.title);
    tv.setText(indention + chapter.text.trim());
    String accessText = "";
    if (chapter.depth == 0 && chapter.subItems().size() > 0) {
      if (chapter.fold) { accessText = "〉"; }
      else { accessText = "﹀"; }
    }
    TextView accessTextView = view.findViewById(R.id.access);
    accessTextView.setText(accessText);
    return view;
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

  }

  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
    return true;
  }
}

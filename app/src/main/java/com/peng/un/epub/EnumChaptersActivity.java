package com.peng.un.epub;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.peng.un.R;

import java.util.List;

public class EnumChaptersActivity extends AppCompatActivity {
  private RecyclerView chaptersRecyclerView;;
  private TextView titleTextView;
  private List<String> chapterTitles;
  private String epubPath;
  private final EnumChaptersHandler enumChaptersHandler = new EnumChaptersHandler();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_enum_chapters);

    // 初始化视图
    chaptersRecyclerView = findViewById(R.id.chapters_recyclerview);
    titleTextView = findViewById(R.id.book_title);

    // 获取传入的epub文件路径
    epubPath = getIntent().getStringExtra("epub_path");

    EnumChaptersAdapter adapter = new EnumChaptersAdapter();
    chaptersRecyclerView.setAdapter(adapter);

    // 加载章节列表
    enumChaptersHandler.loadEPub();

    // 设置列表点击事件
    chaptersRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
      // 这里可以实现点击事件处理逻辑
    });
  }
}

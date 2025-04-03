package com.peng.un.epub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.peng.un.R;

import java.util.ArrayList;
import java.util.List;

public class EnumChaptersActivity extends AppCompatActivity {
    private ListView chaptersListView;
    private TextView titleTextView;
    private List<String> chapterTitles;
    private String epubPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enum_chapters);

        // 初始化视图
        chaptersListView = findViewById(R.id.chapters_listview);
        titleTextView = findViewById(R.id.book_title);

        // 获取传入的epub文件路径
        epubPath = getIntent().getStringExtra("epub_path");
        
        // 加载章节列表
        loadChapters();

        // 设置列表点击事件
        chaptersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void loadChapters() {
    }
}

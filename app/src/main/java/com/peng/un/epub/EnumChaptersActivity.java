package com.peng.un.epub;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peng.un.R;

import java.util.List;

public class EnumChaptersActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_enum_chapters);

    // 初始化视图
    ListView listView = findViewById(R.id.vl_category);
    EnumChaptersAdapter adapter = new EnumChaptersAdapter(this);
    listView.setAdapter(adapter);
    EnumChaptersUIHandler enumChaptersUIHandler = new EnumChaptersUIHandler(this, adapter);
    EnumChaptersHandler enumChaptersHandler = new EnumChaptersHandler();
    enumChaptersHandler.addCallback(enumChaptersUIHandler);


    // 加载章节列表
    enumChaptersHandler.loadEPub();
  }
}

package com.peng.un.path;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.peng.un.R;
import com.peng.un.action.PathExplore;
import com.peng.un.data.Data;
import com.peng.un.data.Settings;
import com.peng.un.epub.EnumChaptersActivity;

import java.util.Collections;
import java.util.List;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.DataViewHolder> {
  private List<Data.File> dataList = Collections.emptyList();
  private PathActivity pathActivity; // 添加 PathActivity 的引用

  // 修改构造函数，接收 PathActivity 实例
  public PathAdapter(PathActivity pathActivity) {
    this.pathActivity = pathActivity;
  }

  public void setData(List<Data.File> dataList) {
    this.dataList = dataList;
    // 这里可以考虑优化，避免使用 notifyDataSetChanged
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.item_file, parent, false);
    return new DataViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
    Data.File data = dataList.get(position);
    String name = data.getName();

    holder.textView.setText(name);
    int id = data.isFolder()? R.drawable.ic_folder : R.drawable.ic_file;
    if (data.getType() == Data.Type.EPUB) {
      id = R.drawable.ic_epub;
    }
    holder.textView.setCompoundDrawablesWithIntrinsicBounds(id,0, 0, 0);

    holder.itemView.setOnClickListener(v -> {
      if (data.isFolder()) {
        java.io.File folderFile = new java.io.File(data.getPath());
        // 这里需要在 PathActivity 中添加一个方法来处理导航逻辑
        if (PathExplore.instance().navigateTo(folderFile)) {
          pathActivity.updateFileList();
        } else {
          Toast.makeText(pathActivity,
            "无法访问该文件夹", Toast.LENGTH_SHORT).show();
        }
      } else {
        if (data.getType() == Data.Type.EPUB) {
          // 创建 Intent 跳转到 EnumChaptersActivity
          Intent intent = new Intent(pathActivity, EnumChaptersActivity.class);
          // 可以传递 EPUB 文件的路径到 EnumChaptersActivity
          intent.putExtra("epubFilePath", data.getPath());
          pathActivity.startActivity(intent);
        } else {
          // 这里需要传入 PathActivity 的上下文
          Toast.makeText(pathActivity,
                  "文件: " + data.getName(), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return dataList.size();
  }

  public static class DataViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    DataViewHolder(View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.textView);
    }
  }
}

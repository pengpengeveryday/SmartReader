package com.peng.un.path;

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

import java.util.Collections;
import java.util.List;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.DataViewHolder> {
    private List<Data.File> dataList = Collections.emptyList();

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

        if (name.equals("..")) {
            holder.textView.setText("↩ 返回上级目录");
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_folder, 0, 0, 0);
            return;
        }

        holder.textView.setText(name);
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(
                data.isFolder() ? R.drawable.ic_folder : R.drawable.ic_file,
                0, 0, 0);

        holder.itemView.setOnClickListener(v -> {
            if (name.equals("..")) {
                java.io.File parentFile = new java.io.File(data.getPath()).getParentFile();
                // 这里需要在 PathActivity 中添加一个方法来处理导航逻辑
                // pathExplore.navigateTo(parentFile);
                // updateFileList();
                return;
            }

            if (data.isFolder()) {
                java.io.File folderFile = new java.io.File(data.getPath());
                // 这里需要在 PathActivity 中添加一个方法来处理导航逻辑
                 if (PathExplore.instance().navigateTo(folderFile)) {
                     // 获取文件列表
                     List<Data.File> dataList = PathExplore.instance().listFiles();
                     // 更新适配器数据
                     setData(dataList);
                 } else {
                     Toast.makeText(Settings.instance().context,
                             "无法访问该文件夹", Toast.LENGTH_SHORT).show();
                 }
            } else {
                // 这里需要传入 PathActivity 的上下文
                 Toast.makeText(Settings.instance().context,
                         "文件: " + data.getName(), Toast.LENGTH_SHORT).show();
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

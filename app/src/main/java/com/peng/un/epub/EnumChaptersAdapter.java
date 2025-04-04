package com.peng.un.epub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

// 定义 EnumChaptersAdapter 类，继承自 ListAdapter
public class EnumChaptersAdapter extends ListAdapter<String, EnumChaptersAdapter.ChapterViewHolder> {

    // 定义 DiffUtil.ItemCallback 用于计算新旧数据的差异
    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            // 判断两个 item 是否代表同一个对象
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            // 判断两个 item 的内容是否相同
            return oldItem.equals(newItem);
        }
    };

    // 构造函数，传入 DiffUtil.ItemCallback
    public EnumChaptersAdapter() {
        super(DIFF_CALLBACK);
    }

    // 创建 ViewHolder
    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载列表项布局
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ChapterViewHolder(view);
    }

    // 绑定数据到 ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        // 获取当前位置的章节名称
        String chapter = getItem(position);
        // 设置章节名称到 TextView
        holder.textView.setText(chapter);
    }

    // 定义 ViewHolder 类
    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        // 用于显示章节名称的 TextView
        TextView textView;

        // ViewHolder 构造函数
        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            // 初始化 TextView
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}

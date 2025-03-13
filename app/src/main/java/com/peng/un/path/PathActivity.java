package com.peng.un.path;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peng.un.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private FileAdapter adapter;
    private File currentDirectory;
    private TextView tvCurrentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        tvCurrentPath = findViewById(R.id.tvCurrentPath);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new FileAdapter();
        recyclerView.setAdapter(adapter);

        checkPermissionAndBrowse();
    }

    private void checkPermissionAndBrowse() {
        if (ContextCompat.checkSelfPermission(this, 
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            initializeFileList();
        }
    }

    private void initializeFileList() {
        // 从外部存储根目录开始浏览
        currentDirectory = Environment.getExternalStorageDirectory();
        updateFileList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeFileList();
            } else {
                Toast.makeText(this, "需要存储权限才能浏览文件", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void updateFileList() {
        tvCurrentPath.setText(currentDirectory.getAbsolutePath());
        File[] files = currentDirectory.listFiles();
        List<File> fileList = new ArrayList<>();
        
        // 添加返回上级目录选项（如果不是在根目录）
        if (currentDirectory.getParentFile() != null) {
            fileList.add(new File(currentDirectory, ".."));
        }
        
        if (files != null) {
            // 先添加文件夹，再添加文件
            List<File> folders = new ArrayList<>();
            List<File> documents = new ArrayList<>();
            
            for (File file : files) {
                if (file.isDirectory()) {
                    folders.add(file);
                } else {
                    documents.add(file);
                }
            }
            
            Collections.sort(folders);
            Collections.sort(documents);
            
            fileList.addAll(folders);
            fileList.addAll(documents);
        }
        
        adapter.setFiles(fileList);
    }

    private class FileAdapter extends RecyclerView.Adapter<FileViewHolder> {
        private List<File> files = new ArrayList<>();

        public void setFiles(List<File> files) {
            this.files = files;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_file, parent, false);
            return new FileViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
            File file = files.get(position);
            String fileName = file.getName();
            
            // 处理返回上级目录的情况
            if (fileName.equals("..")) {
                holder.textView.setText("↩ 返回上级目录");
                holder.textView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_folder, 0, 0, 0);
                return;
            }

            holder.textView.setText(fileName);
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(
                file.isDirectory() ? R.drawable.ic_folder : R.drawable.ic_file,
                0, 0, 0);

            holder.itemView.setOnClickListener(v -> {
                if (fileName.equals("..")) {
                    // 返回上级目录
                    currentDirectory = currentDirectory.getParentFile();
                    updateFileList();
                    return;
                }

                if (file.isDirectory()) {
                    if (file.canRead()) {
                        currentDirectory = file;
                        updateFileList();
                    } else {
                        Toast.makeText(PathActivity.this, 
                            "无法访问该文件夹", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 处理文件点击事件
                    Toast.makeText(PathActivity.this, 
                        "文件: " + file.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return files.size();
        }
    }

    private static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        FileViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
} 
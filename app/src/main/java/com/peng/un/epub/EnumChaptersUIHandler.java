package com.peng.un.epub;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EnumChaptersUIHandler extends Handler implements EnumChaptersHandler.EnumChaptersCallback {
  private final EnumChaptersActivity enumChaptersActivity;
  private final EnumChaptersAdapter adapter;

  private enum EVENT {
    ChaptersUpdate
  }

  EnumChaptersUIHandler(EnumChaptersActivity enumChaptersActivity, EnumChaptersAdapter adapter) {
    this.enumChaptersActivity = enumChaptersActivity;
    this.adapter = adapter;
  }

  @Override
  public void onChaptersUpdate() {
    sendEmptyMessage(EVENT.ChaptersUpdate.ordinal());
  }

  @Override
  public void handleMessage(@NonNull Message msg) {
    EVENT event = EVENT.values()[msg.what];
    if (event == EVENT.ChaptersUpdate) {
      adapter.notifyDataSetChanged();
    }
  }
}

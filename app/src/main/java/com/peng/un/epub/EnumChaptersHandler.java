package com.peng.un.epub;

import android.os.Handler;
import android.os.Message;

import com.peng.un.action.PathExplore;
import com.peng.un.action.epub.EPubExplore;
import com.peng.un.data.EPub;
import com.peng.un.utils.ALog;
import com.peng.un.utils.MessageQueue;

import java.util.ArrayList;
import java.util.List;

public class EnumChaptersHandler implements MessageQueue.MessageCallback {
  public interface EnumChaptersCallback {

  }

  private final List<EnumChaptersCallback> mCallbacks = new ArrayList<>();


  private final MessageQueue mMessageQueue = new MessageQueue("EnumChaptersHandler");
  private final Handler mHandler = mMessageQueue.handler();

  enum MessageType {
    LoadEPub,
  }

  EnumChaptersHandler() {
  }

  /**
   * 添加一个 EnumChaptersCallback 回调到列表中
   * @param callback 要添加的回调实例
   */
  public void addCallback(EnumChaptersCallback callback) {
    if (callback != null && !mCallbacks.contains(callback)) {
      mCallbacks.add(callback);
    }
  }

  void loadEPub() {
    Message msg = Message.obtain(mHandler, MessageType.LoadEPub.ordinal());
    mMessageQueue.postMessage(msg, this);
  }

  @Override
  public void onMessage(Message msg) {
    int what = msg.what;
    MessageType type = MessageType.values()[what];
    switch (type) {
      case LoadEPub:
        onLoadEPub();
        break;
    }

  }

  void onLoadEPub() {
    ALog.d("EnumChaptersHandler: onLoadEPub");
    EPub epub = (EPub) PathExplore.instance().getSelectFile();
    EPubExplore.instance().load(epub);
  }
}

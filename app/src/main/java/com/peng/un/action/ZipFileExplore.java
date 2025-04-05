package com.peng.un.action;

import android.graphics.Bitmap;
import android.util.Size;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZipFileExplore {
  private final String path; final int defaultSize = 1024*1024*10; byte[] cache = new byte[defaultSize];
  private final ArrayList<String> entryNames = new ArrayList<>();
  private List<FileHeader> headers = new ArrayList<>();
  private ZipFile zipFile = null; private final boolean isAsset;
  public ZipFileExplore(String path) {
    if (path.startsWith("/")) {
      this.path = path; isAsset = false;
    } else {
      isAsset = true; this.path = path;
    }
    try {
      zipFile = new ZipFile(this.path);
      if (!zipFile.isValidZipFile()) return;
      headers = zipFile.getFileHeaders();
      for (FileHeader header : headers) {
        //ALog.i(ALog.Category.EPub, "header:" + header.getFileName());
        entryNames.add(header.getFileName());
      }
    } catch (ZipException e) {
      e.printStackTrace();
      zipFile = null;
    }
  }
  public void close() {
  }
  public int size() {
    return entryNames.size();
  }
  public ArrayList<String> entryNames() { return entryNames; }
  public byte[] readByte(String name) {
    if (entryNames.indexOf(name) < 0) { return null; }

    for (FileHeader header : headers) {
      if (header.getFileName().equals(name)) {
        try {
          ZipInputStream zis = zipFile.getInputStream(header);
          ByteArrayOutputStream bos = new ByteArrayOutputStream(defaultSize);
          int reads = zis.read(cache);
          while (reads > 0) {
            //ALog.i(ALog.Category.EPub, "read:" + reads);
            bos.write(cache, 0, reads);
            reads = zis.read(cache);
          }
          return bos.toByteArray();
        } catch (ZipException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }
  public String read(String name) {
    byte[] b = readByte(name);
    return b != null ? new String(b) : "";
  }
  public String[] epubContentList() {
    for (String name : entryNames) {
      String[]subs = name.split("/");
      if (subs[subs.length-1].equalsIgnoreCase("toc.ncx")) {
        String[] result = new String[2];
        result[0] = name; result[1] = read(name);
        return result;
      }
    }
    for (String name : entryNames) {
      String[]subs = name.split("/");
      if (subs[subs.length-1].endsWith(".ncx")) {
        String[] result = new String[2];
        result[0] = name; result[1] = read(name);
        return result;
      }
    }
    for (String name : entryNames) {
      String[]subs = name.split("/");
      if (subs[subs.length-1].equals("nav.xhtml")) {
        String[] result = new String[2];
        result[0] = name; result[1] = read(name);
        return result;
      }
    }
    return null;
  }
}

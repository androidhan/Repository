package com.hanshao.repository.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * AUTHOR: hanshao
 * DATE: 17/6/8.
 * ACTION: 文件管理类
 */
public class FileManager {


  private static FileManager sFileManager;

  public static FileManager instance() {
    if (sFileManager == null) {
      synchronized (FileManager.class) {
        FileManager temp = sFileManager;
        if (temp == null) {
          temp = new FileManager();
          sFileManager = temp;
        }
      }
    }
    return sFileManager;
  }



  private FileManager() {
  }


  void writeToFile(File file, String fileContent) {
    if (!file.exists()) {
      try {
        final FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  String readFileContent(File file) {
    final StringBuilder fileContentBuilder = new StringBuilder();
    if (file.exists()) {
      String stringLine;
      try {
        final FileReader fileReader = new FileReader(file);
        final BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((stringLine = bufferedReader.readLine()) != null) {
          fileContentBuilder.append(stringLine).append("\n");
        }
        bufferedReader.close();
        fileReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return fileContentBuilder.toString();
  }

  boolean exists(File file) {
    return file.exists();
  }

  boolean clearFile(File file) {
    if (file.exists()) {
      file.delete();
      return true;
    }
    return false;
  }

  boolean clearDirectory(File directory) {
    boolean result = false;
    if (directory.exists()) {
      for (File file : directory.listFiles()) {
        result = file.delete();
      }
    }
    return result;
  }

  void writeToPreferences(Context context, String preferenceFileName, String key,
                          long value) {

    final SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName,
            Context.MODE_PRIVATE);
    final SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putLong(key, value);
    editor.apply();
  }

  long getFromPreferences(Context context, String preferenceFileName, String key) {
    final SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName,
            Context.MODE_PRIVATE);
    return sharedPreferences.getLong(key, 0);
  }
}

package com.hanshao.repository.cache.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hanshao.repository.cache.FileManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Serializer {


  private static Serializer sSerializer;
  private Gson mGson = new Gson();

  public static Serializer instance() {
    if (sSerializer == null) {
      synchronized (FileManager.class) {
        Serializer temp = sSerializer;
        if (temp == null) {
          temp = new Serializer();
          sSerializer = temp;
        }
      }
    }
    return sSerializer;
  }

  private Serializer(){}


  public String serialize(Object object) {
    return mGson.toJson(object);
  }

  public <T> T deserialize(String string, Class<T> clazz) {
    return mGson.fromJson(string, clazz);
  }


  public <T> List<T> deserializeToList(String json, Class<T> clazz) {

    Type type = new TypeToken<ArrayList<JsonObject>>() {
    }.getType();
    ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

    ArrayList<T> arrayList = new ArrayList<>();
    for (JsonObject jsonObject : jsonObjects) {
      arrayList.add(new Gson().fromJson(jsonObject, clazz));
    }
    return arrayList;
  }
}

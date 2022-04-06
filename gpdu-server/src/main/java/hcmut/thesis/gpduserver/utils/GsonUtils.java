package hcmut.thesis.gpduserver.utils;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.Objects;

public final class GsonUtils {

  final Gson builder;
  final Gson gsonSnakeCase;

  private static GsonUtils instance = null;

  private static GsonUtils getInstance() {
    if (Objects.isNull(instance)) {
      instance = new GsonUtils();
    }
    return instance;
  }

  private GsonUtils() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    builder = gsonBuilder.disableHtmlEscaping().create();

    gsonSnakeCase = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
  }

  public static String toJsonString(Object obj) {
    return getInstance().builder.toJson(obj);
  }

  public static <T> T fromJsonString(String sJson, Class<T> t) {
    return getInstance().builder.fromJson(sJson, t);
  }

  public static <T> T fromJson(String json, Type typeOfT) {
    return getInstance().builder.fromJson(json, typeOfT);
  }

  public static <T> T fromJsonStringSnakeCase(String sJson, Class<T> t) {
    return getInstance().gsonSnakeCase.fromJson(sJson, t);
  }

  public static <T> T fromJsonSnakeCase(String json, Type typeOfT) {
    return getInstance().gsonSnakeCase.fromJson(json, typeOfT);
  }

}

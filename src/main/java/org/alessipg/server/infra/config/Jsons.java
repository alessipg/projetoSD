package org.alessipg.server.infra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// importe/adapters extras (LocalDateTime, etc.) se necessário

public final class Jsons {
  private static final Gson GSON = new GsonBuilder()
      // .registerTypeAdapter(LocalDateTime.class, ...)
      // .serializeNulls()
      // .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
      .create();

  private Jsons() {}

  public static Gson get() {
    return GSON;
  }
}
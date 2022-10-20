package com.intellias.intellistart.interviewplanning.controllers.dtos.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FBUser {

  private Data data;

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class Data {
    private String app_id;
    private String type;
    private String application;
    private long data_access_expires_at;
    private long expires_at;
    private boolean is_valid;
    private String[] scopes;
    private String user_id;
  }
}

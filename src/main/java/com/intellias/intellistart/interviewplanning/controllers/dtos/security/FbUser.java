package com.intellias.intellistart.interviewplanning.controllers.dtos.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO object for Facebook User.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FbUser {

  private Data data;

  /**
   * Inner DTO class for FbUser DTO.
   */
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class Data {

    @JsonAlias("app_id")
    private String appId;
    private String type;
    private String application;

    @JsonAlias("data_access_expires_at")
    private long dataAccessExpiresAt;

    @JsonAlias("expires_at")
    private long expiresAt;

    @JsonAlias("is_valid")
    private boolean isValid;
    private String[] scopes;

    @JsonAlias("user_id")
    private String userId;
  }
}

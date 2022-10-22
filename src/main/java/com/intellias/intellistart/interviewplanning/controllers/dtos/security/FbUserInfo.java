package com.intellias.intellistart.interviewplanning.controllers.dtos.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO object for Facebook User Information.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FbUserInfo {

  private String email;
  private String name;

  @JsonAlias("user_id")
  private String userId;
}

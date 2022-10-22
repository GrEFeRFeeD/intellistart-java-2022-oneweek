package com.intellias.intellistart.interviewplanning.controllers.dtos.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO object for Facebook Marker.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FbMarker {

  @JsonAlias("access_token")
  private String accessToken;

  @JsonAlias("token_type")
  private String tokenType;
}

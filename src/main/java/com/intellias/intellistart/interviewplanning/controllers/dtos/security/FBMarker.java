package com.intellias.intellistart.interviewplanning.controllers.dtos.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FBMarker {

  private String access_token;
  private String token_type;
}

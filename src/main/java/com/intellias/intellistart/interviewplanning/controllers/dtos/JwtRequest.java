package com.intellias.intellistart.interviewplanning.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DAO for Facebook Token.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtRequest {

  private String facebookToken;
}

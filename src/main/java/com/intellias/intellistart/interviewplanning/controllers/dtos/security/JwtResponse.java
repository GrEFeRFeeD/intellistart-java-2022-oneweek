package com.intellias.intellistart.interviewplanning.controllers.dtos.security;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO object for JWT response.
 */
@AllArgsConstructor
@Getter
@Setter
public class JwtResponse implements Serializable {

  private final String jwttoken;
}
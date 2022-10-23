package com.intellias.intellistart.interviewplanning.controllers.dtos.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO object for User.
 */
// TODO: remove
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

  private String username;
  private String role;

}
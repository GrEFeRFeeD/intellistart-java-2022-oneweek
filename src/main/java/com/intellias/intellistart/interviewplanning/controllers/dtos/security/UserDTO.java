package com.intellias.intellistart.interviewplanning.controllers.dtos.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
	private String username;
	private String role;

}
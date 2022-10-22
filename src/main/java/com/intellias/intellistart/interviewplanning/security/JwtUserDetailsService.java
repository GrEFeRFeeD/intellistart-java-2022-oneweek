package com.intellias.intellistart.interviewplanning.security;

import com.intellias.intellistart.interviewplanning.controllers.dtos.security.UserDto;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService that implement work with JWT.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);

    Set<GrantedAuthority> authorities = new HashSet<>();

    if (user != null && user.getRole() != null) {
      authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    return new org.springframework.security.core.userdetails.User(email,
        passwordEncoder().encode(email),
        authorities);
  }

  // TODO: DELETE

  /**
   * Be.
   */
  public User save(UserDto user) {
    User newUser = new User();
    newUser.setEmail(user.getUsername());
    newUser.setRole(Role.valueOf(user.getRole()));
    return userRepository.save(newUser);
  }
}
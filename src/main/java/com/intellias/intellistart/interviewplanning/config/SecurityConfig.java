package com.intellias.intellistart.interviewplanning.config;

import com.intellias.intellistart.interviewplanning.security.FilterChainExceptionHandler;
import com.intellias.intellistart.interviewplanning.security.JwtAuthenticationEntryPoint;
import com.intellias.intellistart.interviewplanning.security.JwtRequestFilter;
import com.intellias.intellistart.interviewplanning.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final UserDetailsService jwtUserDetailsService;
  private final JwtRequestFilter jwtRequestFilter;
  private final FilterChainExceptionHandler filterChainExceptionHandler;
  private final PasswordEncoder passwordEncoder;

  /**
   * Constructor.
   */
  @Autowired
  public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
      UserDetailsService jwtUserDetailsService, JwtRequestFilter jwtRequestFilter,
      FilterChainExceptionHandler filterChainExceptionHandler, PasswordEncoder passwordEncoder) {
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
    this.filterChainExceptionHandler = filterChainExceptionHandler;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Defining the custom UserDetailsService and password encoder.
   */
  @Bean
  public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
        AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(jwtUserDetailsService)
        .passwordEncoder(passwordEncoder);
    return authenticationManagerBuilder.build();
  }

  /**
   * Configuring requests security.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // We don't need CSRF for this example
    http.csrf().disable()
        // dont authenticate this particular request
        .authorizeRequests().antMatchers("/authenticate", "/register").permitAll()
        .antMatchers("/test").hasRole("INTERVIEWER")
        .antMatchers("/testa").hasAuthority("TEST")
        // all other requests need to be authenticated
        .anyRequest().authenticated().and()
        // make sure we use stateless session; session won't be used to
        // store user's state.
        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // Add a filter to validate the tokens with every request
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        // ExceptionHandler filter
        .addFilterBefore(filterChainExceptionHandler, JwtRequestFilter.class);

    return http.build();
  }

}

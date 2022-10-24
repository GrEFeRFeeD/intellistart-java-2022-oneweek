package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.exceptions.SecurityException;
import com.intellias.intellistart.interviewplanning.exceptions.SecurityException.SecurityExceptionProfile;
import com.intellias.intellistart.interviewplanning.security.JwtUserDetailsService;
import com.intellias.intellistart.interviewplanning.utils.FacebookUtil;
import com.intellias.intellistart.interviewplanning.utils.FacebookUtil.FacebookScopes;
import com.intellias.intellistart.interviewplanning.utils.JwtTokenUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

/**
 * Controller for authentication and authenticated requests.
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final JwtUserDetailsService userDetailsService;
  private final FacebookUtil facebookUtil;

  /**
   * Constructor.
   */
  @Autowired
  public JwtAuthenticationController(AuthenticationManager authenticationManager,
      JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService,
      FacebookUtil facebookUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
    this.facebookUtil = facebookUtil;
  }

  /**
   * Method that mappings the authentication request through generating
   * JWT by Facebook Token.
   *
   * @param facebookToken gained by user oauth2 token
   * @return JWT
   */
  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody String facebookToken) {

    Map<FacebookScopes, String> userScopes;
    try {
      userScopes = facebookUtil
          .getScope(facebookToken);
    } catch (RestClientException e) {
      throw new SecurityException(SecurityExceptionProfile.BAD_FACEBOOK_TOKEN);
    }

    String email = userScopes.get(FacebookScopes.EMAIL);
    String name = userScopes.get(FacebookScopes.NAME);

    authenticate(email);

    final UserDetails userDetails = userDetailsService
        .loadUserByEmailAndName(email, name);

    final String token = jwtTokenUtil.generateToken(userDetails, name);

    return ResponseEntity.ok(token);
  }

  private void authenticate(String username) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, username));
    } catch (BadCredentialsException e) {
      throw new SecurityException(SecurityExceptionProfile.BAD_CREDENTIALS);
    }
  }
}
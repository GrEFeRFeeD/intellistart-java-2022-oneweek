package com.intellias.intellistart.interviewplanning.utils;

import com.intellias.intellistart.interviewplanning.controllers.dtos.security.FbMarker;
import com.intellias.intellistart.interviewplanning.controllers.dtos.security.FbUser;
import com.intellias.intellistart.interviewplanning.controllers.dtos.security.FbUserInfo;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Util class for work with Facebook Token.
 */
@Service
public class FacebookUtil {

  private static final String markerUri = "https://graph.facebook.com/oauth/access_token?"
      + "client_id=%s&client_secret=%s&grant_type=client_credentials";
  private static final String checkMarkerUri = "https://graph.facebook.com/debug_token?input_token=%s&access_token=%s";
  private static final String userUri = "https://graph.facebook.com/%s?fields=name,email&access_token=%s";

  @Value("${spring.security.oauth2.client.registration.facebook.clientId}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.facebook.clientSecret}")
  private String clientSecret;

  /**
   * Gets application marker from Facebook API.
   *
   * @return application marker
   */
  private String getMarker() {

    String uri = String.format(markerUri, clientId, clientSecret);
    RestTemplate restTemplate = new RestTemplate();
    FbMarker marker = restTemplate.getForObject(uri, FbMarker.class);

    return marker == null ? "null" : marker.getAccessToken();
  }

  /**
   * Gets user information object from Facebook by given Facebook User Token.
   *
   * @param token user facebook token
   * @return user information object
   */
  public FbUser getFbUser(String token) {

    String uri = String.format(checkMarkerUri, token, getMarker());
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForObject(uri, FbUser.class);
  }

  /**
   * Gets user name and email from Facebook by Facebook User Token.
   *
   * @param token user facebook token
   * @return user email
   */
  public Map<FacebookScopes, String> getScope(String token)  {

    FbUser fbUser = getFbUser(token);
    String userId = fbUser.getData().getUserId();
    String uri = String.format(userUri, userId, token);
    RestTemplate restTemplate = new RestTemplate();
    FbUserInfo fbUserInfo = restTemplate.getForObject(uri, FbUserInfo.class);

    Map<FacebookScopes, String> scopes = new HashMap<>();
    if (fbUserInfo != null) {
      scopes.put(FacebookScopes.EMAIL, fbUserInfo.getEmail());
      scopes.put(FacebookScopes.NAME, fbUserInfo.getName());
    }

    return scopes;
  }

  /**
   * Enum for application facebook scopes.
   */
  public static enum FacebookScopes {
    EMAIL, NAME
  }
}

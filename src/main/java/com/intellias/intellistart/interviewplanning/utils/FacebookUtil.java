package com.intellias.intellistart.interviewplanning.utils;

import com.intellias.intellistart.interviewplanning.controllers.dtos.security.FBMarker;
import com.intellias.intellistart.interviewplanning.controllers.dtos.security.FBUser;
import com.intellias.intellistart.interviewplanning.controllers.dtos.security.FBUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookUtil {

  @Value("${spring.security.oauth2.client.registration.facebook.clientId}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.facebook.clientSecret}")
  private String clientSecret;

  private static final String markerUri = "https://graph.facebook.com/oauth/access_token?"
      + "client_id=%s&client_secret=%s&grant_type=client_credentials";
  private static final String checkMarkerUri = "https://graph.facebook.com/debug_token?input_token=%s&access_token=%s";
  private static final String userUri = "https://graph.facebook.com/%s?fields=email&access_token=%s";

  private String getMarker() {

    String uri = String.format(markerUri, clientId, clientSecret);
    RestTemplate restTemplate = new RestTemplate();
    FBMarker marker = restTemplate.getForObject(uri, FBMarker.class);

    return marker == null ? "be" : marker.getAccess_token();
  }

  public FBUser getFBUser(String token) {

    String uri = String.format(checkMarkerUri, token, getMarker());
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForObject(uri, FBUser.class);
  }

  public String getEmail(String token) {

    FBUser fbUser = getFBUser(token);
    String userId = fbUser.getData().getUser_id();
    String uri = String.format(userUri, userId, token);
    RestTemplate restTemplate = new RestTemplate();
    FBUserInfo fbUserInfo = restTemplate.getForObject(uri, FBUserInfo.class);
    return fbUserInfo == null ? "null" : fbUserInfo.getEmail();
  }

}

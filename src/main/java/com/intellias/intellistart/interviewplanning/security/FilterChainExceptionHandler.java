package com.intellias.intellistart.interviewplanning.security;

import com.intellias.intellistart.interviewplanning.exceptions.SecurityException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class FilterChainExceptionHandler extends OncePerRequestFilter {


  private final HandlerExceptionResolver resolver;

  @Autowired
  public FilterChainExceptionHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (SecurityException exception) {
      resolver.resolveException(request, response, null, exception);
    }
  }
}
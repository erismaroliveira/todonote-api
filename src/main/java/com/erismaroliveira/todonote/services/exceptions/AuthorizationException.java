package com.erismaroliveira.todonote.services.exceptions;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthorizationException extends AccessDeniedException {

  public AuthorizationException(String message) {
    super(message);
  }
  
}

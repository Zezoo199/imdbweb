package com.imdb.imdbweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoActorFoundException extends Exception {
  public NoActorFoundException() {}

  public NoActorFoundException(String msg) {
    super(msg);
  }
}

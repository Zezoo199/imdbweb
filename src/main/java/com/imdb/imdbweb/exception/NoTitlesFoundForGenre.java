package com.imdb.imdbweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoTitlesFoundForGenre extends Exception {
  public NoTitlesFoundForGenre() {
    super();
  }

  public NoTitlesFoundForGenre(String msg) {
    super(msg);
  }
}

package com.imdb.imdbweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ImdbwebApplication {

  public static void main(String[] args) {
    SpringApplication.run(ImdbwebApplication.class, args);
  }
}

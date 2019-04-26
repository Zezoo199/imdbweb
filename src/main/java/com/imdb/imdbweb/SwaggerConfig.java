package com.imdb.imdbweb;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  /**
   * Main Swagger API Bean
   *
   * @return Docket
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.imdb.imdbweb."))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

  /**
   * Method describing api info.
   *
   * @return ApiInfo with parameters describing the application
   */
  private ApiInfo apiInfo() {
    return new ApiInfo(
        "IMDB Based Web Application",
        "Using data of IMDB you can make some queries about movies/actors...etc",
        "1",
        "Terms of service",
        new Contact("Mohamed Abdelaziz", "www.imdb.com", "m.aziz.selim@outlook.com"),
        "License of API",
        "#",
        Collections.emptyList());
  }
}

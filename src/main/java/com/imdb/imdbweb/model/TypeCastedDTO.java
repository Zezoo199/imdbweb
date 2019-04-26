package com.imdb.imdbweb.model;

public class TypeCastedDTO {
  private Boolean isTypeCasted;
  private String genre;

  public TypeCastedDTO(Boolean isTypeCasted, String genre) {
    this.isTypeCasted = isTypeCasted;
    this.genre = genre;
  }

  public Boolean getTypeCasted() {
    return isTypeCasted;
  }

  public void setTypeCasted(Boolean typeCasted) {
    isTypeCasted = typeCasted;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }
}

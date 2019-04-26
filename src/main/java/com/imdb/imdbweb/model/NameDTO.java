package com.imdb.imdbweb.model;

public class NameDTO {
  private String id;
  private String name;
  private String knownForTitles;
  private String profession;

  public NameDTO() {
    // Batch
  }

  public NameDTO(String id, String name, String knownForTitles, String profession) {
    this.id = id;
    this.name = name;
    this.knownForTitles = knownForTitles;
    this.profession = profession;
  }

  public String getProfession() {
    return profession;
  }

  public void setProfession(String profession) {
    this.profession = profession;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKnownForTitles() {
    return knownForTitles;
  }

  public void setKnownForTitles(String knownForTitles) {
    this.knownForTitles = knownForTitles;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}

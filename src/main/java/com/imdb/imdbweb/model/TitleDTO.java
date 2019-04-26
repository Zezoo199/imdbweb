package com.imdb.imdbweb.model;

import java.util.Comparator;

public class TitleDTO implements Comparable<TitleDTO> {
  private String titleId;

  private String titleName;

  private double rating;

  private String genres;

  private String type;

  private String startYear;

  public TitleDTO() {}

  public TitleDTO(
      String titleId, String titleName, double weightedRating, String genres, String startYear) {
    this.titleId = titleId;
    this.titleName = titleName;
    this.rating = weightedRating;
    this.genres = genres;
    this.startYear = startYear;
  }

  public String getStartYear() {
    return startYear;
  }

  public void setStartYear(String startYear) {
    this.startYear = startYear;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitleId() {
    return titleId;
  }

  public void setTitleId(String titleId) {
    this.titleId = titleId;
  }

  public String getTitleName() {
    return titleName;
  }

  public void setTitleName(String titleName) {
    this.titleName = titleName;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public String getGenres() {
    return genres;
  }

  public void setGenres(String genres) {
    this.genres = genres;
  }

  @Override
  public int compareTo(TitleDTO o) {
    return Comparator.comparing(TitleDTO::getRating).reversed().compare(this, o);
  }
}

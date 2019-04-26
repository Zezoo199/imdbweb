package com.imdb.imdbweb.dataobject;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "titles")
public class TitleDAO {
  @Id private String id;

  private String title;

  private String genres;

  private String startYear;

  private BigDecimal weightedRating;

  private String type;

  public BigDecimal getWeightedRating() {
    return weightedRating;
  }

  public String getStartYear() {
    return startYear;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setStartYear(String startYear) {
    this.startYear = startYear;
  }

  public void setWeightedRating(BigDecimal weightedRating) {
    this.weightedRating = weightedRating;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getGenres() {
    return genres;
  }

  public void setGenres(String genres) {
    this.genres = genres;
  }
}

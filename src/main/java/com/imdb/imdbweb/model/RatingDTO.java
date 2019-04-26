package com.imdb.imdbweb.model;

import java.math.BigDecimal;

public class RatingDTO {

  public RatingDTO() { // Batch
  }

  private String tconst;

  private double rating;

  private double numOfVotes;

  private BigDecimal weightedRating;

  public double getNumOfVotes() {
    return numOfVotes;
  }

  public void setNumOfVotes(double numOfVotes) {
    this.numOfVotes = numOfVotes;
  }

  public BigDecimal getWeightedRating() {
    return weightedRating;
  }

  public void setWeightedRating(BigDecimal weightedRating) {
    this.weightedRating = weightedRating;
  }

  public String getTconst() {
    return tconst;
  }

  public void setTconst(String tconst) {
    this.tconst = tconst;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }
}

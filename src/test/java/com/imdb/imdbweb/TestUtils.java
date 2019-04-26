package com.imdb.imdbweb;

import com.imdb.imdbweb.dataobject.NameDAO;
import com.imdb.imdbweb.dataobject.TitleDAO;
import java.math.BigDecimal;

public class TestUtils {

  public static TitleDAO givenTitleMovie(String id, String genre, double rating) {
    TitleDAO titleDAO = new TitleDAO();
    titleDAO.setGenres(genre);
    titleDAO.setId(id);
    titleDAO.setTitle("Film");
    titleDAO.setStartYear("2019");
    titleDAO.setType("movie");
    titleDAO.setWeightedRating(BigDecimal.valueOf(rating));
    return titleDAO;
  }

  public static NameDAO givenNameActor(String id, String name, String knownForTitles) {
    NameDAO nameDAO = new NameDAO();
    nameDAO.setId(id);
    nameDAO.setName(name);
    nameDAO.setProfession("actor");
    nameDAO.setKnownForTitles(knownForTitles);
    return nameDAO;
  }
}

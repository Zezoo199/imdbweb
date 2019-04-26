package com.imdb.imdbweb.service;

import com.imdb.imdbweb.exception.NoActorFoundException;
import com.imdb.imdbweb.exception.NoTitlesFoundForGenre;
import com.imdb.imdbweb.model.NameDTO;
import com.imdb.imdbweb.model.TitleDTO;
import com.imdb.imdbweb.model.TypeCastedDTO;
import java.util.List;

public interface ImdbService {
  /**
   * Service method to get top 250 movies Sorting is handled on database currently
   *
   * @param genre given genre
   * @return List of top rated titles
   * @throws NoTitlesFoundForGenre If nothing found
   */
  List<TitleDTO> getTop250MoviesByGenre(String genre) throws NoTitlesFoundForGenre;

  /**
   * Finds degree of Kevin Bacon for Given Actor Unique ID this method use the filled in memory
   * repository to find the Degree of seperation
   *
   * @param actorId unique Id for actor
   * @return number of degree or -1 if more than 6
   * @throws NoActorFoundException
   */
  Integer findDegreeToKevinBacon(String actorId) throws NoActorFoundException;

  /**
   * Helper method to get all names for given name for the case of similar names
   *
   * @param name
   * @return
   * @throws NoActorFoundException
   */
  List<NameDTO> findAllActorsForName(String name) throws NoActorFoundException;

  /**
   * Method to check if given ActorId is Typecasted by counting all genres for actor and comparing
   * it to the halfcount of his titles
   *
   * @param actorId
   * @return TypeCastDTO with Boolean for typecasted and String for Genre if Typecasted was true
   * @throws NoActorFoundException
   */
  TypeCastedDTO isTypeCasted(String actorId) throws NoActorFoundException;
}

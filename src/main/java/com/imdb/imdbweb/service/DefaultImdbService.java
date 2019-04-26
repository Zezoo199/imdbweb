package com.imdb.imdbweb.service;

import com.imdb.imdbweb.dataobject.NameDAO;
import com.imdb.imdbweb.dataobject.TitleDAO;
import com.imdb.imdbweb.exception.NoActorFoundException;
import com.imdb.imdbweb.exception.NoTitlesFoundForGenre;
import com.imdb.imdbweb.model.NameDTO;
import com.imdb.imdbweb.model.TitleDTO;
import com.imdb.imdbweb.model.TypeCastedDTO;
import com.imdb.imdbweb.repository.KevinBaconRepository;
import com.imdb.imdbweb.repository.NamesRepository;
import com.imdb.imdbweb.repository.TitlesRepository;
import com.imdb.imdbweb.service.mapping.NameMapper;
import com.imdb.imdbweb.service.mapping.NameMapperImpl;
import com.imdb.imdbweb.service.mapping.TitleMapper;
import com.imdb.imdbweb.service.mapping.TitleMapperImpl;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Main service for application */
@Service
public class DefaultImdbService implements ImdbService {

  @Autowired private TitlesRepository titlesRepository;

  @Autowired private NamesRepository namesRepository;

  private TitleMapper titleMapper = new TitleMapperImpl();
  private NameMapper nameMapper = new NameMapperImpl();

  Logger log = LoggerFactory.getLogger(this.getClass());

  /**
   * Service method to get top 250 movies Sorting is handled on database currently
   *
   * @param genre given genre
   * @return List of top rated titles
   * @throws NoTitlesFoundForGenre If nothing found
   */
  public List<TitleDTO> getTop250MoviesByGenre(String genre) throws NoTitlesFoundForGenre {
    log.info("Started finding top rated for genre {}", genre);
    genre = Character.toUpperCase(genre.charAt(0)) + genre.substring(1).toLowerCase();

    List<TitleDAO> titlesForGenre =
        titlesRepository.findTop250ByTypeAndGenresContainingOrderByWeightedRatingDesc(
            "movie", genre);
    if (titlesForGenre.isEmpty()) throw new NoTitlesFoundForGenre("No genre found for " + genre);
    List<TitleDTO> titles =
        titlesForGenre
            .parallelStream()
            .map(title -> titleMapper.mapToDTO(title))
            .collect(Collectors.toList());

    log.info("Found {} title(s) and returning", titlesForGenre.size());
    return titles;
  }
  /**
   * Finds degree of Kevin Bacon for Given Actor Unique ID this method use the filled in memory
   * repository to find the Degree of seperation
   *
   * @param actorId unique Id for actor
   * @return number of degree or -1 if more than 6
   * @throws NoActorFoundException
   */
  @Override
  public Integer findDegreeToKevinBacon(String actorId) throws NoActorFoundException {
    log.info("Finding bacon degree for {}", actorId);
    if (!namesRepository.findById(actorId).isPresent()) {
      throw new NoActorFoundException("No actor found for " + actorId);
    }
    return KevinBaconRepository.getKevinBaconRelationMap().entrySet().stream()
        .filter(pair -> pair.getValue().contains(actorId))
        .findAny()
        .map(Map.Entry::getKey)
        .orElse(-1);
  }

  /**
   * Helper method to get all names for given name for the case of similar names
   *
   * @param givenName
   * @return
   * @throws NoActorFoundException
   */
  @Override
  public List<NameDTO> findAllActorsForName(String givenName) throws NoActorFoundException {
    log.info("Finding all actors with name {}", givenName);
    List<NameDTO> names =
        namesRepository.findAllByProfessionContainingAndName("act", givenName.toUpperCase())
            .stream()
            .map(name -> nameMapper.mapToDTO(name))
            .collect(Collectors.toList());
    names.stream()
        .forEach(
            nameDTO -> {
              replaceTitleIdWithNames(nameDTO);
            });
    if (names.isEmpty()) throw new NoActorFoundException("No actor found for " + givenName);
    log.info("Found {} actors with name {}", names.size(), givenName);
    return names;
  }

  private void replaceTitleIdWithNames(NameDTO name) {
    String newKnownFor = "";
    for (String title : name.getKnownForTitles().split(",")) {
      Optional<TitleDAO> titleDAO = titlesRepository.findById(title);
      if (titleDAO.isPresent()) {
        newKnownFor += titleDAO.get().getTitle() + ",";
      }
    }
    name.setKnownForTitles(newKnownFor);
  }

  /**
   * Method to check if given ActorId is Typecasted by counting all genres for actor and comparing
   * it to the halfcount of his titles
   *
   * @param actorId
   * @return TypeCastDTO with Boolean for typecasted and String for Genre if Typecasted was true
   * @throws NoActorFoundException
   */
  @Override
  public TypeCastedDTO isTypeCasted(String actorId) throws NoActorFoundException {
    Optional<NameDAO> actor = namesRepository.findById(actorId);
    log.info("Checking type casting for {}", actorId);
    if (!actor.isPresent())
      throw new NoActorFoundException("The actor ID should have been correct from frontEnd!");
    String[] knownFor = actor.get().getKnownForTitles().split(",");
    if (knownFor.length == 0) {
      log.warn("The actor ID {} Has no knownForTitles", actorId);
      return new TypeCastedDTO(false, "");
    }
    int halfCount = knownFor.length % 2 == 0 ? (knownFor.length / 2) : ((knownFor.length + 1) / 2);
    if (halfCount == 1) return new TypeCastedDTO(false, ""); // edge case ?
    Map<String, Integer> genresCount = getGenreCountForTitles(knownFor);
    if (genresCount.entrySet().stream().max(Map.Entry.comparingByValue()).isPresent()) {
      Map.Entry<String, Integer> maximumGenreEntry =
          genresCount.entrySet().stream().max(Map.Entry.comparingByValue()).get();
      if (maximumGenreEntry.getValue() >= halfCount)
        return new TypeCastedDTO(true, maximumGenreEntry.getKey());
      else return new TypeCastedDTO(false, "");
    } else return new TypeCastedDTO(false, "");
  }

  /**
   * Method to get all genres for titles array by iterating and appending count in map
   *
   * @param knownFor
   * @return Map of genre and count of appearance of this genre for actor
   */
  private Map<String, Integer> getGenreCountForTitles(String[] knownFor) {
    Map<String, Integer> genresCount = new HashMap<>();
    for (String title : knownFor) {
      Optional<TitleDAO> titleById = titlesRepository.findById(title);
      if (titleById.isPresent()) {
        Arrays.asList(titleById.get().getGenres().split(",")).stream()
            .forEach(
                genre -> {
                  if (genresCount.containsKey(genre))
                    genresCount.put(genre, genresCount.get(genre) + 1);
                  else genresCount.put(genre, 1);
                });
      }
    }
    return genresCount;
  }
}

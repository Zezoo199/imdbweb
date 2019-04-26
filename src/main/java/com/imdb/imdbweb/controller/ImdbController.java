package com.imdb.imdbweb.controller;

import com.imdb.imdbweb.exception.NoActorFoundException;
import com.imdb.imdbweb.exception.NoTitlesFoundForGenre;
import com.imdb.imdbweb.model.NameDTO;
import com.imdb.imdbweb.model.TitleDTO;
import com.imdb.imdbweb.model.TypeCastedDTO;
import com.imdb.imdbweb.service.ImdbService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Main Rest Controller Imdb */
@RestController
@RequestMapping("imdb")
public class ImdbController {

  @Autowired private ImdbService imdbService;

  /**
   * Rest GET Method to get TopRated 250 Movie for a given genre path : /topRated/{genre}
   *
   * @param genre given genre
   * @return List of top rated titles
   * @throws NoTitlesFoundForGenre If nothing found
   */
  @ApiOperation(value = "Find top 250 rated movies for a given genre")
  @ApiResponse(code = 404, message = "If no movies found or bad genre")
  @GetMapping("/topRated/{genre}")
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody List<TitleDTO> getTopRatedMovies(@PathVariable String genre)
      throws NoTitlesFoundForGenre {
    return imdbService.getTop250MoviesByGenre(genre);
  }

  /**
   * Finds degree of Kevin Bacon for Given Actor Unique ID path : /degreeToKevinBacon
   *
   * @param actorId unique Id for actor
   * @return Number of degree and -1 if more than 6
   */
  @ApiOperation(
      value =
          "Return degree of seperation from 1 to 6 between given actor and Kevin Bacon , -1 if no degree")
  @GetMapping("/degreeToKevinBacon")
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody Integer degreeToKevinBacon(@RequestParam String actorId)
      throws NoActorFoundException {
    return imdbService.findDegreeToKevinBacon(actorId);
  }

  /**
   * Helper method to get all names for given name for the case of similar names path:
   * /allNamesforName
   *
   * @param actorName given actor name
   * @return List of Names found with that name
   * @throws NoActorFoundException
   */
  @ApiOperation(
      value = "Helper method to get all names for given name for the case of similar names")
  @ApiResponse(code = 404, message = "If no actors found with name")
  @GetMapping("/allNamesforName")
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody List<NameDTO> getNameIdsForName(@RequestParam String actorName)
      throws NoActorFoundException {
    return imdbService.findAllActorsForName(actorName);
  }

  /**
   * Method to check if given ActorId is Typecasted (Half or more than of his titles are one Genre)
   * path /isTypeCasted
   *
   * @param actorId
   * @return TypeCastedDTO with Boolean for typecasted and String for Genre if Typecasted was true
   * @throws NoActorFoundException
   */
  @ApiOperation(
      value = "Check if actorId is typeCasted  (Half or more than of his titles are one Genre)")
  @ApiResponse(code = 404, message = "If no actors found with name")
  @GetMapping("/isTypeCasted")
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody TypeCastedDTO isTypeCasted(@RequestParam String actorId)
      throws NoActorFoundException {
    return imdbService.isTypeCasted(actorId);
  }
}

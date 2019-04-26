package com.imdb.imdbweb.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.imdb.imdbweb.exception.NoActorFoundException;
import com.imdb.imdbweb.exception.NoTitlesFoundForGenre;
import com.imdb.imdbweb.model.NameDTO;
import com.imdb.imdbweb.model.TitleDTO;
import com.imdb.imdbweb.model.TypeCastedDTO;
import com.imdb.imdbweb.service.ImdbService;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ImdbController.class)
public class ImdbControllerTest {
  @Autowired private MockMvc mvc;

  @MockBean private ImdbService imdbService;

  @Test
  public void givenMoviesAndGenre_whenTopRated_thenReturnJsonArray() throws Exception {
    // given service two movies
    List<TitleDTO> allTitles = givenTitles();
    given(imdbService.getTop250MoviesByGenre(any())).willReturn(allTitles);

    // when and then
    mvc.perform(get("/imdb/topRated/Drama").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].titleName", is(allTitles.get(0).getTitleName())))
        .andExpect(jsonPath("$[1].titleName", is(allTitles.get(1).getTitleName())));
  }

  @Test
  public void givenGenreWithNoMovies_whenTopRated_thenReturnNotFound() throws Exception {
    // given service will throw exception
    given(imdbService.getTop250MoviesByGenre("Comedy")).willThrow(NoTitlesFoundForGenre.class);

    // when and then
    mvc.perform(get("/imdb/topRated/Comedy").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void givenServiceReturnTwo_whenDegreeToKevinBacon_thenReturnJsonTwo() throws Exception {
    given(imdbService.findDegreeToKevinBacon("nm0003")).willReturn(2);
    mvc.perform(
            get("/imdb/degreeToKevinBacon")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", "nm0003"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(2)));
  }

  @Test
  public void giveSimilarnNames_whenGetNameIdsForName_thenReturnAllNames() throws Exception {
    // given service will return names
    List<NameDTO> names = givenSimilarNames();
    given(imdbService.findAllActorsForName("Tom Hardy")).willReturn(names);

    // when and then
    mvc.perform(
            get("/imdb/allNamesforName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorName", "Tom Hardy"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is(names.get(0).getName())))
        .andExpect(jsonPath("$[1].knownForTitles", is("tt003,tt004")));
  }

  @Test
  public void giveSimilarnNames_whenGetNameIdsForNotFoundName_thenReturnNotFound()
      throws Exception {
    // given service will throw Exception
    given(imdbService.findAllActorsForName("Mohamed Abdelaziz"))
        .willThrow(NoActorFoundException.class);

    // when and then
    mvc.perform(
            get("/imdb/allNamesforName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorName", "Mohamed Abdelaziz"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void givenServiceReturnTypeCasted_whenIsTypeCasted_thenReturnJson() throws Exception {
    // given service will throw Exception
    given(imdbService.isTypeCasted("nm009")).willReturn(new TypeCastedDTO(Boolean.TRUE, "Comedy"));

    // when and then
    mvc.perform(
            get("/imdb/isTypeCasted")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", "nm009"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.typeCasted", is(true)))
        .andExpect(jsonPath("$.genre", is("Comedy")));
  }

  @Test
  public void giveNoNamesAndTitles_whenIsTypeCasted_thenReturnNotFound() throws Exception {
    // given service will throw Exception
    given(imdbService.isTypeCasted("nm009")).willThrow(NoActorFoundException.class);

    // when and then
    mvc.perform(
            get("/imdb/isTypeCasted")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", "nm009"))
        .andExpect(status().isNotFound());
  }

  private List<TitleDTO> givenTitles() {
    TitleDTO movie1 = new TitleDTO("tt001", "The Shawshank Redemption", 9.5, "Drama", "1999");
    TitleDTO movie2 = new TitleDTO("tt002", "The Lord of the Rings", 9.0, "Drama", "2003");
    return Arrays.asList(movie1, movie2);
  }

  private List<NameDTO> givenSimilarNames() {
    NameDTO name1 = new NameDTO("nm001", "Tom Hardy", "tt001,tt002", "actor");
    NameDTO name2 = new NameDTO("nm002", "Tom Hardy", "tt003,tt004", "actor");
    return Arrays.asList(name1, name2);
  }
}

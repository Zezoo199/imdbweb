package com.imdb.imdbweb;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ImdbwebIntegrationTests {

  @Autowired private MockMvc mvc;

  @Test
  public void givenTestFilesImported_whenFindAllForNames_thenReturnJsonArray() throws Exception {
    // given Imported test files

    // when and then
    mvc.perform(
            get("/imdb/allNamesforName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorName", "Tom Hardy"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("TOM HARDY")))
        .andExpect(
            jsonPath(
                "$[1].knownForTitles",
                is("Corbett and Courtney Before the Kinetograph,Miss Jerry,")));
  }

  @Test
  public void givenTestFilesImported_whenDegreeToKevinBacon_thenReturnCorrect() throws Exception {
    // given Imported test files

    // when find kevin  for nm0000001 then 1
    mvc.perform(
            get("/imdb/degreeToKevinBacon")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", "nm0000001"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(1)));
    // when find kevin for  nm0000002 then 2
    mvc.perform(
            get("/imdb/degreeToKevinBacon")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", "nm0000002"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(2)));

    // when find kevin for  nm0000003 then 3
    mvc.perform(
            get("/imdb/degreeToKevinBacon")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", "nm0000003"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(3)));

    // when find kevin for nm0000004 then  4
    mvc.perform(
            get("/imdb/degreeToKevinBacon")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", "nm0000004"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(4)));
  }

  @Test
  public void givenTestFilesImported_whenDegreeToKevinBaconNotsavedActor_then404NotFound()
      throws Exception {
    mvc.perform(
            get("/imdb/degreeToKevinBacon")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", "nm0000099"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void givenFiles_whenIsTypeCasted_thenReturnTrue() throws Exception {
    // given
    String typeCasteActor = "nm0000001";
    // when and then
    mvc.perform(
            get("/imdb/isTypeCasted")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", typeCasteActor))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.typeCasted", is(true)))
        .andExpect(jsonPath("$.genre", is("Animation")));
  }

  @Test
  public void givenFiles_whenIsNotTypeCasted_thenReturnFalse() throws Exception {
    // given
    String notTypeCasteActor = "nm0000005";
    // when and then
    mvc.perform(
            get("/imdb/isTypeCasted")
                .contentType(MediaType.APPLICATION_JSON)
                .param("actorId", notTypeCasteActor))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.typeCasted", is(false)))
        .andExpect(jsonPath("$.genre", is("")));
  }

  @Test
  public void givenFiles_whenFindTop250_thenReturnJsonArraySorted() throws Exception {
    mvc.perform(get("/imdb/topRated/Drama").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].titleName", is("Shawshank Redemption")))
        .andExpect(jsonPath("$[1].titleName", is("Pauvre Pierrot")))
        .andExpect(jsonPath("$[2].titleName", is("Le clown et ses chiens")));
  }
}

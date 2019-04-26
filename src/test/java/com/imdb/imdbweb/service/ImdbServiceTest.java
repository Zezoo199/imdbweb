package com.imdb.imdbweb.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.imdb.imdbweb.TestUtils;
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
import java.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class ImdbServiceTest {
  @Mock private TitlesRepository titlesRepository;
  @Mock private NamesRepository namesRepository;

  @InjectMocks private DefaultImdbService imdbService;

  @Before
  public void setUp() {}

  @Test
  public void givenTwoMoviesInRepository_whenGetTop250MoviesByGenre_thenReturnMapped()
      throws Exception {
    // given
    givenTitlesInRepository();

    // when
    List<TitleDTO> comedy = imdbService.getTop250MoviesByGenre("Comedy");

    // then
    Assert.assertTrue(!comedy.isEmpty() && comedy.size() == 2);
    Assert.assertTrue(comedy.get(0).getTitleId().equals("tt001"));
    Assert.assertTrue(comedy.get(1).getTitleId().equals("tt002"));
  }

  @Test(expected = NoTitlesFoundForGenre.class)
  public void givenNoMoviesForGenre_whenGetTop250Movies_thenThrowException()
      throws NoTitlesFoundForGenre {
    // Given no movies for

    // when
    List<TitleDTO> unknownGenre = imdbService.getTop250MoviesByGenre("unknownGenre");

    // then expect exception

  }

  @Test
  public void givenTwoActorsWithSimilarName_whenfindAllActorsForName_thenReturnBoth()
      throws Exception {
    // given
    givenNamesInRepository();

    // when
    List<NameDTO> findNames = imdbService.findAllActorsForName("Jessica Steve");

    // then
    Assert.assertTrue(!findNames.isEmpty() && findNames.size() == 2);
    Assert.assertTrue(findNames.get(0).getId().equals("nm001"));
    Assert.assertTrue(findNames.get(1).getId().equals("nm002"));
  }

  @Test(expected = NoActorFoundException.class)
  public void givenNoNames_whenFindAllActorsForName_thenThrowException()
      throws NoActorFoundException {
    // given No Names

    // when
    List<NameDTO> findNames = imdbService.findAllActorsForName("Mohamed Abdedlaziz");

    // then expect exception

  }

  @Test
  public void giveTypeCasted_whenIsTypeCasted_thenReturnTrue() throws Exception {
    // Given Names and titles where nm001 is typecasted in Comedy
    givenNamesInRepository();
    givenTitlesInRepository();

    // when
    TypeCastedDTO isTypeCastedMap = imdbService.isTypeCasted("nm001");

    // then
    Assert.assertTrue(isTypeCastedMap != null);
    Assert.assertTrue(
        isTypeCastedMap.getTypeCasted() == true && isTypeCastedMap.getGenre().equals("Comedy"));
  }

  @Test
  public void giveNotTypeCasted_whenIsTypeCasted_thenReturnFalse() throws Exception {
    // Given Names and titles where nm001 is typecasted in Comedy
    givenNamesInRepository();
    givenTitlesInRepository();

    // when
    TypeCastedDTO isTypeCastedMap = imdbService.isTypeCasted("nm002");

    // then
    Assert.assertTrue(isTypeCastedMap != null);
    Assert.assertTrue(
        isTypeCastedMap.getTypeCasted() == false && isTypeCastedMap.getGenre().equals(""));
  }

  @Test(expected = NoActorFoundException.class)
  public void giveNotFoundActor_whenIsTypeCasted_thenThrowException() throws NoActorFoundException {
    // Given Names and titles where nm001 is typecasted in Comedy
    givenNamesInRepository();
    givenTitlesInRepository();

    // when
    TypeCastedDTO isTypeCastedMap = imdbService.isTypeCasted("nm009");

    // then exception
  }

  @Test
  public void givenActorWithNoTitles_whenIsTypeCasted_thenFalse() throws NoActorFoundException {
    // Given Names and titles where nm001 is typecasted in Comedy
    givenNamesInRepository();
    givenTitlesInRepository();

    // when
    TypeCastedDTO isTypeCastedMap = imdbService.isTypeCasted("nm003");

    // then
    Assert.assertTrue(isTypeCastedMap != null);
    Assert.assertTrue(
        isTypeCastedMap.getTypeCasted() == false && isTypeCastedMap.getGenre().equals(""));
  }

  @Test
  public void givenBaconMap_whenfindDegreeToKevinBacon_thenReturnCorrectNumber()
      throws NoActorFoundException {
    givenNamesInRepository();
    givenTitlesInRepository();
    givenKevinBaconMap();

    // when
    Integer degree = imdbService.findDegreeToKevinBacon("nm001");

    // then
    Assert.assertTrue(degree == 2);
  }

  @Test(expected = NoActorFoundException.class)
  public void givenBaconMapAndNotFoundName_whenfindDegreeToKevinBacon_thenException()
      throws NoActorFoundException {
    givenNamesInRepository();
    givenTitlesInRepository();
    givenKevinBaconMap();

    // when not found name or degree higher than 6
    Integer degree = imdbService.findDegreeToKevinBacon("nm007");

    // then

  }

  private void givenKevinBaconMap() {
    Set<String> namesLevel2 = new HashSet<>();
    namesLevel2.add("nm001");
    namesLevel2.add("nm002");

    Map<Integer, Set<String>> kevinMap = new HashMap<>();
    kevinMap.put(2, namesLevel2);
    KevinBaconRepository.setKevinBaconRelationMap(kevinMap);
  }

  private void givenNamesInRepository() {
    List<NameDAO> givenDatabaseNames = new ArrayList<>();

    NameDAO name1 = TestUtils.givenNameActor("nm001", "Jessica Steve", "tt001,tt002,tt003");
    givenDatabaseNames.add(name1);

    NameDAO name2 = TestUtils.givenNameActor("nm002", "Jessica Steve", "tt02,tt003,tt004");
    NameDAO name3 = TestUtils.givenNameActor("nm003", "Tom Hardy", "");

    givenDatabaseNames.add(name2);

    when(namesRepository.findAllByProfessionContainingAndName(anyString(), anyString()))
        .thenReturn(givenDatabaseNames);
    when(namesRepository.findById("nm001")).thenReturn(Optional.of(name1));
    when(namesRepository.findById("nm002")).thenReturn(Optional.of(name2));
    when(namesRepository.findById("nm003")).thenReturn(Optional.of(name3));
  }

  private void givenTitlesInRepository() {
    List<TitleDAO> givenDatabaseTitles = new ArrayList<>();

    TitleDAO title1 = TestUtils.givenTitleMovie("tt001", "Comedy", 9);
    givenDatabaseTitles.add(title1);

    TitleDAO title2 = TestUtils.givenTitleMovie("tt002", "Comedy", 7);
    givenDatabaseTitles.add(title2);

    when(titlesRepository.findTop250ByTypeAndGenresContainingOrderByWeightedRatingDesc(
            anyString(), anyString()))
        .thenReturn(givenDatabaseTitles);

    when(titlesRepository.findAllByTypeAndGenresContaining(anyString(), anyString()))
        .thenReturn(givenDatabaseTitles);
    when(titlesRepository.findById("tt001")).thenReturn(Optional.of(title1));

    when(titlesRepository.findById("tt002")).thenReturn(Optional.of(title2));
  }
}

package com.imdb.imdbweb.repository;

import com.imdb.imdbweb.TestUtils;
import com.imdb.imdbweb.dataobject.NameDAO;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NamesRepositoryTest {
  @Autowired private TestEntityManager entityManager;

  @Autowired private NamesRepository namesRepository;

  @Before
  public void fillDatabase() {
    givenDatabaseWithTwoSimilarNames();
  }

  @Test
  public void givenTwoActors_whenfindAllByProfessionContainingAndName_thenReturnBoth() {
    // given filled database

    // when
    List<NameDAO> allByProfessionContainingAndName =
        namesRepository.findAllByProfessionContainingAndName("act", "JESSICA COMMON");

    // then
    Assert.assertTrue(
        allByProfessionContainingAndName.size() == 2
            && allByProfessionContainingAndName.get(0).getId().equals("nm003")
            && allByProfessionContainingAndName.get(0).getProfession().equals("actor"));
    Assert.assertTrue(allByProfessionContainingAndName.get(1).getId().equals("nm004"));
  }

  @Test
  public void givenTwoActors_whenFindAllWithNamesAndTitles_thenReturn() {
    // given filled database

    // when
    List<Object[]> allNamesWithTitles = namesRepository.findAllNamesWithTitles();

    // then
    Assert.assertTrue(allNamesWithTitles.size() == 2);
    String castedNameId = (String) allNamesWithTitles.get(0)[0];
    String castedTitles = (String) allNamesWithTitles.get(0)[1];
    Assert.assertTrue(castedNameId.equals("nm003"));
    Assert.assertTrue(castedTitles.equals("tt001,tt005"));
  }

  private void givenDatabaseWithTwoSimilarNames() {
    NameDAO name1 = TestUtils.givenNameActor("nm003", "JESSICA COMMON", "tt001,tt005");
    NameDAO name2 = TestUtils.givenNameActor("nm004", "JESSICA COMMON", "tt006,tt007");

    entityManager.persist(name1);
    entityManager.persist(name2);
    entityManager.flush();
  }
}

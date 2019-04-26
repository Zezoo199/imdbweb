package com.imdb.imdbweb.repository;

import com.imdb.imdbweb.TestUtils;
import com.imdb.imdbweb.dataobject.TitleDAO;
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
public class TitlesRepositoryTest {
  @Autowired private TestEntityManager entityManager;

  @Autowired private TitlesRepository titlesRepository;

  @Before
  public void fillDatabase() {
    givenDatabaseWithSomeMovies();
  }

  @Test
  public void givenSomeMovies_whenfindTop250Ordered_thenReturnSorted() {
    // given filled database

    // when
    List<TitleDAO> top250 =
        titlesRepository.findTop250ByTypeAndGenresContainingOrderByWeightedRatingDesc(
            "movie", "Action");

    // then second inserted should be first
    Assert.assertTrue(top250.size() == 2);
    Assert.assertTrue(top250.get(0).getId().equals("tt002"));
  }

  private void givenDatabaseWithSomeMovies() {
    TitleDAO title1 = TestUtils.givenTitleMovie("tt001", "Comedy,Drama,Action", 7);
    TitleDAO title2 = TestUtils.givenTitleMovie("tt002", "Horror,Action", 9);

    entityManager.persist(title1);
    entityManager.persist(title2);
    entityManager.flush();
  }
}

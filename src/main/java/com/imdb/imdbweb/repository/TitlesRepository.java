package com.imdb.imdbweb.repository;

import com.imdb.imdbweb.dataobject.TitleDAO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface TitlesRepository extends JpaRepository<TitleDAO, String> {

  List<TitleDAO> findAllByTypeAndGenresContaining(String type, String genre);

  List<TitleDAO> findAllByType(String type);

  List<TitleDAO> findTop250ByTypeAndGenresContainingOrderByWeightedRatingDesc(
      String type, String genre);
}

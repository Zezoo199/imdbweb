package com.imdb.imdbweb.repository;

import com.imdb.imdbweb.dataobject.NameDAO;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NamesRepository extends JpaRepository<NameDAO, String> {
  List<NameDAO> findAllByIdIn(Collection<String> id);

  List<NameDAO> findAllByKnownForTitlesContaining(String title);

  List<NameDAO> findAllByProfessionContainingAndName(String profession, String givenName);

  @Query(value = "select id, knownForTitles FROM names")
  List<Object[]> findAllNamesWithTitles();
}

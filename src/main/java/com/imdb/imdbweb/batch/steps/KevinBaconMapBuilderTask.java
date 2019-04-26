package com.imdb.imdbweb.batch.steps;

import com.imdb.imdbweb.repository.KevinBaconRepository;
import com.imdb.imdbweb.repository.NamesRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/** Task to build Kevin Bacon map on start in memory for fastest response time */
@Component
public class KevinBaconMapBuilderTask implements Tasklet {
  public static final String KEVIN_BACON_ID = "nm0000102";
  @Autowired private NamesRepository namesRepository;

  Map<String, Set<String>> names = null;
  Logger log = LoggerFactory.getLogger(this.getClass());

  @Nullable
  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    names = new HashMap<>();
    names = fetchAllNamesFromRepoAndReverseByTitleAsKey();
    KevinBaconRepository.setKevinBaconRelationMap(prepapreAllKevinBaconDegreesByBFS(names));
    return RepeatStatus.FINISHED;
  }

  private Map<String, Set<String>> fetchAllNamesFromRepoAndReverseByTitleAsKey() {
    return namesRepository
        .findAllNamesWithTitles()
        .parallelStream()
        .map(nameDAO -> mapAndReverseActorRow(nameDAO))
        .flatMap(e -> e)
        .collect(
            Collectors.groupingBy(
                e -> e.getKey(), Collectors.mapping(e -> e.getValue(), Collectors.toSet())));
  }

  /**
   * Method to build the map from all names in the repository using BFS starting from Kevin Bacon
   * and all actors acted with him as level 1 and so on
   *
   * @param names reversed grouped names as title,name found from repository
   * @return map of integer as level of seperation and set of all actors with that level
   */
  private Map<Integer, Set<String>> prepapreAllKevinBaconDegreesByBFS(
      Map<String, Set<String>> names) {

    Instant start = Instant.now();
    log.info("building KevinBaconRelationMap...");
    Queue<Set<String>> queue = new LinkedList<>();
    Map<Integer, Set<String>> kevinMap = new HashMap<>();

    queue.add(getTitles(names, KEVIN_BACON_ID));

    int depth = 1;

    Set<String> visitedTitles = new HashSet<>();
    while (depth < 7) {
      kevinMap.put(depth, new HashSet<>());
      Set<String> titles = queue.poll();
      for (String title : titles) {
        if (!visitedTitles.contains(title)) {
          visitedTitles.add(title);
          kevinMap.get(depth).addAll(names.get(title));
        }
      }

      queue.add(getTitles(names, kevinMap.get(depth)));

      depth++;
    }
    Instant end = Instant.now();
    log.info(
        "It took {} ms to build KevinBaconRelationMap.", Duration.between(start, end).toMillis());
    names.clear();
    return kevinMap;
  }

  /**
   * Helper method to find all titles for single actor
   *
   * @param names
   * @param actor
   * @return Set of titles
   */
  private Set<String> getTitles(Map<String, Set<String>> names, String actor) {
    return names
        .entrySet()
        .parallelStream()
        .filter(e -> e.getValue().contains(actor))
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }
  /**
   * Helper method to find all titles for Set of actors
   *
   * @param names
   * @param actors
   * @return Set of titles
   */
  private Set<String> getTitles(Map<String, Set<String>> names, Set<String> actors) {
    return names
        .entrySet()
        .parallelStream()
        .filter(e -> e.getValue().parallelStream().anyMatch(actors::contains))
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }

  /**
   * Method to reverse a row of name and known for titles
   *
   * @param nameDAO given row
   * @return Stream of Map Entry grouped by title
   */
  private Stream<Map.Entry<String, String>> mapAndReverseActorRow(Object[] nameDAO) {
    Map<String, String> map = new HashMap<>();
    String knownFor = (String) nameDAO[1];
    String nameId = (String) nameDAO[0];
    String[] titles = knownFor.split(",");
    for (int i = 0; i < titles.length; i++) {
      map.put(titles[i], nameId);
    }
    return map.entrySet().stream();
  }
}

package com.imdb.imdbweb.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Static singleton repository to hold kevin bacon relations */
public class KevinBaconRepository {
  private static Map<Integer, Set<String>> kevinBaconRelationMap;

  private KevinBaconRepository() {}

  public static Map<Integer, Set<String>> getKevinBaconRelationMap() {
    if (kevinBaconRelationMap == null) kevinBaconRelationMap = new HashMap<>();
    return kevinBaconRelationMap;
  }

  public static void setKevinBaconRelationMap(Map<Integer, Set<String>> map) {
    kevinBaconRelationMap = map;
  }
}

package com.imdb.imdbweb.dataobject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "names")
@Table(name = "names")
public class NameDAO {
  @Id private String id;
  private String name;
  private String knownForTitles;
  private String profession;

  public String getProfession() {
    return profession;
  }

  public void setProfession(String profession) {
    this.profession = profession;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKnownForTitles() {
    return knownForTitles;
  }

  public void setKnownForTitles(String knownForTitles) {
    this.knownForTitles = knownForTitles;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}

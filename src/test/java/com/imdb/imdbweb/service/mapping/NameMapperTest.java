package com.imdb.imdbweb.service.mapping;

import com.imdb.imdbweb.TestUtils;
import com.imdb.imdbweb.dataobject.NameDAO;
import com.imdb.imdbweb.model.NameDTO;
import org.junit.Assert;
import org.junit.Test;

public class NameMapperTest {
  private NameMapper nameMapper = new NameMapperImpl();

  @Test
  public void mapToDTO() {
    // given
    NameDAO name1 = TestUtils.givenNameActor("nm001", "Jessica Steve", "tt001,tt002,tt003");

    // when
    NameDTO nameDTO = nameMapper.mapToDTO(name1);

    // then
    Assert.assertTrue(nameDTO != null);
    Assert.assertTrue(
        nameDTO.getId().equals("nm001")
            && nameDTO.getName().equals("Jessica Steve")
            && nameDTO.getKnownForTitles().equals("tt001,tt002,tt003"));
  }
}

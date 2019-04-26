package com.imdb.imdbweb.service.mapping;

import com.imdb.imdbweb.TestUtils;
import com.imdb.imdbweb.dataobject.TitleDAO;
import com.imdb.imdbweb.model.TitleDTO;
import org.junit.Assert;
import org.junit.Test;

public class TitleMapperTest {
  private TitleMapper titleMapper = new TitleMapperImpl();

  @Test
  public void mapToDTO() {
    // given
    TitleDAO title = TestUtils.givenTitleMovie("tt002", "Comedy", 7);
    // when
    TitleDTO nameDTO = titleMapper.mapToDTO(title);

    // then
    Assert.assertTrue(nameDTO != null);
    Assert.assertTrue(
        nameDTO.getTitleId().equals("tt002")
            && nameDTO.getRating() == 7
            && nameDTO.getGenres().equals("Comedy"));
  }
}

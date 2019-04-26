package com.imdb.imdbweb.service.mapping;

import com.imdb.imdbweb.dataobject.TitleDAO;
import com.imdb.imdbweb.model.TitleDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Service;

@Service
@Mapper
@MapperConfig(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class TitleMapper {

  public TitleDTO mapToDTO(TitleDAO titleDAO) {
    return map(titleDAO);
  }

  @Mapping(target = "rating", source = "weightedRating")
  @Mapping(target = "titleName", source = "title")
  @Mapping(target = "titleId", source = "id")
  protected abstract TitleDTO map(TitleDAO titleDAO);
}

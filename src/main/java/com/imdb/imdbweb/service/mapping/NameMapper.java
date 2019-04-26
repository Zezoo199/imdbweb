package com.imdb.imdbweb.service.mapping;

import com.imdb.imdbweb.dataobject.NameDAO;
import com.imdb.imdbweb.model.NameDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Service;

@Service
@Mapper
@MapperConfig(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class NameMapper {
  public NameDTO mapToDTO(NameDAO nameDAO) {
    return map(nameDAO);
  }

  protected abstract NameDTO map(NameDAO nameDAO);
}

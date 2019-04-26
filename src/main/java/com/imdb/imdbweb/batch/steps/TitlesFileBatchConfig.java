package com.imdb.imdbweb.batch.steps;

import com.imdb.imdbweb.model.TitleDTO;
import com.imdb.imdbweb.repository.TitlesRepository;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;

@Configuration
public class TitlesFileBatchConfig {
  @Autowired private DataSource dataSource;
  @Autowired private TitlesRepository titlesRepository;
  Logger log = LoggerFactory.getLogger(this.getClass());

  @Bean
  public FlatFileItemReader<TitleDTO> getTitleReader() {
    FlatFileItemReader<TitleDTO> reader = new FlatFileItemReader<>();
    int count = (int) titlesRepository.count();
    log.info("Found {} Titles to skip", count);
    reader.setLinesToSkip(count + 1);
    reader.setResource(new ClassPathResource("title.basics.tsv"));
    reader.setLineMapper(lineMapper());
    return reader;
  }

  private DefaultLineMapper<TitleDTO> lineMapper() {
    DefaultLineMapper<TitleDTO> titleLineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer("\t");
    lineTokenizer.setStrict(false);
    lineTokenizer.setIncludedFields(0, 1, 2, 5, 8);
    lineTokenizer.setNames("titleId", "type", "titleName", "startYear", "genres");
    titleLineMapper.setLineTokenizer(lineTokenizer);

    BeanWrapperFieldSetMapper<TitleDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(TitleDTO.class);

    titleLineMapper.setFieldSetMapper(fieldSetMapper);

    return titleLineMapper;
  }

  @Bean
  public ItemProcessor<TitleDTO, TitleDTO> getTitleProcessor() {
    return new ItemProcessor<TitleDTO, TitleDTO>() {
      @Nullable
      @Override
      public TitleDTO process(TitleDTO item) throws Exception {
        return item;
      }
    };
  }

  @Bean
  public JdbcBatchItemWriter<TitleDTO> getTitleDatabaseWriter() {
    JdbcBatchItemWriter<TitleDTO> titleDatabaseWriter = new JdbcBatchItemWriter<>();
    titleDatabaseWriter.setItemSqlParameterSourceProvider(
        new BeanPropertyItemSqlParameterSourceProvider<>());
    titleDatabaseWriter.setSql(
        "INSERT INTO titles (id,type,title,genres,start_year) VALUES (:titleId, :type, :titleName, :genres, :startYear)");
    titleDatabaseWriter.setDataSource(dataSource);
    return titleDatabaseWriter;
  }
}

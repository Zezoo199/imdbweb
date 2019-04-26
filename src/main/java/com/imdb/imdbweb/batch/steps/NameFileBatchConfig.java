package com.imdb.imdbweb.batch.steps;

import com.imdb.imdbweb.model.NameDTO;
import com.imdb.imdbweb.repository.NamesRepository;
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
public class NameFileBatchConfig {
  @Autowired private DataSource dataSource;
  @Autowired private NamesRepository namesRepository;
  Logger log = LoggerFactory.getLogger(this.getClass());

  @Bean
  public FlatFileItemReader<NameDTO> getNameLineReader() {
    FlatFileItemReader<NameDTO> reader = new FlatFileItemReader<>();
    int count = (int) namesRepository.count();
    log.info("Found {} Names to skip", count);
    reader.setLinesToSkip(count + 1);

    reader.setResource(new ClassPathResource("name.basics.tsv"));
    /*
    It also could be like below so files are not in resources for easy refresh of data
    reader.setResource(new FileUrlResource("/some/path"));
    */
    reader.setLineMapper(lineMapper());
    return reader;
  }

  private DefaultLineMapper<NameDTO> lineMapper() {
    DefaultLineMapper<NameDTO> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer("\t");
    lineTokenizer.setStrict(false);

    lineTokenizer.setIncludedFields(0, 1, 4, 5);
    lineTokenizer.setNames("id", "name", "profession", "knownForTitles");
    lineMapper.setLineTokenizer(lineTokenizer);
    BeanWrapperFieldSetMapper<NameDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(NameDTO.class);
    lineMapper.setFieldSetMapper(fieldSetMapper);

    return lineMapper;
  }

  @Bean
  public ItemProcessor<NameDTO, NameDTO> getNameProcessor() {
    return new ItemProcessor<NameDTO, NameDTO>() {
      @Nullable
      @Override
      public NameDTO process(NameDTO name) throws Exception {
        String upperCaseName = name.getName().toUpperCase();
        return new NameDTO(
            name.getId(), upperCaseName, name.getKnownForTitles(), name.getProfession());
      }
    };
  }

  @Bean
  public JdbcBatchItemWriter<NameDTO> getNameDatabaseWriter() {
    JdbcBatchItemWriter<NameDTO> nameDataBaseWriter = new JdbcBatchItemWriter<>();
    nameDataBaseWriter.setItemSqlParameterSourceProvider(
        new BeanPropertyItemSqlParameterSourceProvider<NameDTO>());
    nameDataBaseWriter.setSql(
        "INSERT  INTO names (id, name,profession, known_for_titles) VALUES (:id, :name, :profession, :knownForTitles)");
    nameDataBaseWriter.setDataSource(dataSource);
    return nameDataBaseWriter;
  }
}

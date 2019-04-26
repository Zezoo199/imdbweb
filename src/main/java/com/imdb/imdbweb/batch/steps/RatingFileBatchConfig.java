package com.imdb.imdbweb.batch.steps;

import com.imdb.imdbweb.model.RatingDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class RatingFileBatchConfig {

  private static final int MIN_OF_VOTES = 25000;
  private static final double CONSTANT_RATING = 7.0;

  @Autowired private DataSource dataSource;
  Logger log = LoggerFactory.getLogger(this.getClass());

  @Bean
  public FlatFileItemReader<RatingDTO> getRatingReader() {
    FlatFileItemReader<RatingDTO> reader = new FlatFileItemReader<>();
    reader.setLinesToSkip(1);
    reader.setResource(new ClassPathResource("title.ratings.tsv"));
    reader.setLineMapper(lineMapper());
    return reader;
  }

  private DefaultLineMapper<RatingDTO> lineMapper() {
    DefaultLineMapper<RatingDTO> ratingLineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer("\t");
    lineTokenizer.setStrict(false);
    lineTokenizer.setIncludedFields(0, 1, 2);
    lineTokenizer.setNames("tconst", "rating", "numOfVotes");
    ratingLineMapper.setLineTokenizer(lineTokenizer);

    BeanWrapperFieldSetMapper<RatingDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(RatingDTO.class);

    ratingLineMapper.setFieldSetMapper(fieldSetMapper);

    return ratingLineMapper;
  }

  @Bean
  public ItemProcessor<RatingDTO, RatingDTO> getRatingProcessor() {
    return new ItemProcessor<RatingDTO, RatingDTO>() {
      @Nullable
      @Override
      public RatingDTO process(RatingDTO item) throws Exception {
        //  log.info("Processing rating id {},", item.getTconst());
        item.setWeightedRating(calculateWeightedRating(item.getRating(), item.getNumOfVotes()));
        return item;
      }
    };
  }

  @Bean
  public JdbcBatchItemWriter<RatingDTO> getRatingDatabaseWriter() {
    JdbcBatchItemWriter<RatingDTO> titleDatabaseWriter = new JdbcBatchItemWriter<>();
    titleDatabaseWriter.setItemSqlParameterSourceProvider(
        new BeanPropertyItemSqlParameterSourceProvider<>());
    titleDatabaseWriter.setSql(
        "UPDATE titles set weighted_rating = :weightedRating where id = :tconst");
    titleDatabaseWriter.setDataSource(dataSource);
    return titleDatabaseWriter;
  }

  private BigDecimal calculateWeightedRating(double givenRating, double numOfVotes) {
    double compute =
        numOfVotes / (numOfVotes + MIN_OF_VOTES) * givenRating
            + (MIN_OF_VOTES / (numOfVotes + MIN_OF_VOTES)) * CONSTANT_RATING;
    // Go equation from https://www.quora.com/How-does-IMDbs-rating-system-work
    return BigDecimal.valueOf(compute).setScale(2, RoundingMode.HALF_UP);
  }
}

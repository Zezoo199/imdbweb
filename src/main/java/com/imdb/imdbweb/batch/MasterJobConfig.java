package com.imdb.imdbweb.batch;

import com.imdb.imdbweb.batch.steps.*;
import com.imdb.imdbweb.model.NameDTO;
import com.imdb.imdbweb.model.RatingDTO;
import com.imdb.imdbweb.model.TitleDTO;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

/** Master Batch processing Beans */
@EnableBatchProcessing
@Configuration
public class MasterJobConfig {
  @Autowired private JobBuilderFactory jobBuilderFactory;

  @Autowired private StepBuilderFactory stepBuilderFactory;

  @Autowired private RatingFileBatchConfig ratingFileBatchConfig;

  @Autowired private NameFileBatchConfig nameFileBatchConfig;

  @Autowired private TitlesFileBatchConfig titlesFileBatchConfig;

  @Autowired private DataSource dataSource;

  @Bean
  public Step namesFileToDatabaseStep() {
    return stepBuilderFactory
        .get("namesFileToDatabaseStep")
        .<NameDTO, NameDTO>chunk(1)
        .reader(nameFileBatchConfig.getNameLineReader())
        .processor(nameFileBatchConfig.getNameProcessor())
        .writer(nameFileBatchConfig.getNameDatabaseWriter())
        .build();
  }

  @Bean
  public Step titlesFileToDatabaseStep() {
    return stepBuilderFactory
        .get("titleFileToDatabase")
        .<TitleDTO, TitleDTO>chunk(1)
        .reader(titlesFileBatchConfig.getTitleReader())
        .processor(titlesFileBatchConfig.getTitleProcessor())
        .writer(titlesFileBatchConfig.getTitleDatabaseWriter())
        .build();
  }

  @Bean
  public Step ratingsFileToDatabaseStep() {
    return stepBuilderFactory
        .get("ratingFileToDatabase")
        .<RatingDTO, RatingDTO>chunk(1)
        .reader(ratingFileBatchConfig.getRatingReader())
        .processor(ratingFileBatchConfig.getRatingProcessor())
        .writer(ratingFileBatchConfig.getRatingDatabaseWriter())
        .build();
  }

  @Bean
  public Step kevinBaconMapBuilderStep() {
    return stepBuilderFactory
        .get("buildKevinBaconMapBuilder")
        .tasklet(kevinBaconMapBuilderTasklet())
        .build();
  }

  @Bean
  public Step createDBIndexesTask() {
    return stepBuilderFactory.get("createDatabaseIndexes").tasklet(databaseIndexesTask()).build();
  }

  @Bean
  public KevinBaconMapBuilderTask kevinBaconMapBuilderTasklet() {
    return new KevinBaconMapBuilderTask();
  }

  @Bean
  public CreateDatabaseIndexesTask databaseIndexesTask() {
    return new CreateDatabaseIndexesTask();
  }

  @Bean
  public Flow flowTitles() {
    return new FlowBuilder<SimpleFlow>("flowTitles").start(titlesFileToDatabaseStep()).build();
  }

  @Bean
  public Flow flowNames() {
    return new FlowBuilder<SimpleFlow>("flowNames").start(namesFileToDatabaseStep()).build();
  }

  /**
   * Split flow for parallel execution for Names and Titles files
   *
   * @return Parallel Flow
   */
  @Bean
  public Flow parallelFlowForTitlesAndNames() {
    return new FlowBuilder<SimpleFlow>("parallelFlowForTitlesAndNames")
        .split(taskExecutor())
        .add(flowNames(), flowTitles())
        .build();
  }

  @Bean
  public TaskExecutor taskExecutor() {
    return new SimpleAsyncTaskExecutor("ImportToDatabase");
  }

  @Bean
  Job masterJobFilesToDatabases(JobCompletionNotificationListener listener) {
    return jobBuilderFactory
        .get("importFilesToDBAndApplyIndexes")
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .start(parallelFlowForTitlesAndNames())
        .next(kevinBaconMapBuilderStep())
        .next(ratingsFileToDatabaseStep())
        .next(createDBIndexesTask())
        .build()
        .build();
  }
}

package com.imdb.imdbweb.batch.steps;

import com.imdb.imdbweb.repository.NamesRepository;
import javax.sql.DataSource;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/** Tasklet for database Index creation */
@Component
public class CreateDatabaseIndexesTask implements Tasklet {
  @Autowired private NamesRepository namesRepository;
  @Autowired private DataSource dataSource;

  @Nullable
  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    ScriptUtils.executeSqlScript(
        dataSource.getConnection(), new ClassPathResource("indexesScript"));
    return RepeatStatus.FINISHED;
  }
}

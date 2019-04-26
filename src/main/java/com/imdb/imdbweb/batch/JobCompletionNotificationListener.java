package com.imdb.imdbweb.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

/** Listener to print log when all Steps finishes */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info(
          "============ Job {}  Finished ============\n",
          jobExecution.getJobInstance().getJobName());
    }
  }
}

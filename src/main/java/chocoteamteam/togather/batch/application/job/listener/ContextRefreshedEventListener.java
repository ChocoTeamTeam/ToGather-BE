package chocoteamteam.togather.batch.application.job.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Date;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    private final JobExplorer jobExplorer;
    private final JobRepository jobRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (String jobName : jobExplorer.getJobNames()) {
            Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(jobName);// 실행중인 job exc

            for (JobExecution jobExecution : runningJobExecutions) {
                log.warn("job id : {} is re start!", jobExecution.getJobId());
                jobExecution.setStatus(BatchStatus.STOPPED); //failed or stopped
                jobExecution.setEndTime(new Date());
                for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
                    if (stepExecution.getStatus().isRunning()) { // 실행 중인 step stop 처리
                        stepExecution.setStatus(BatchStatus.STOPPED);
                        stepExecution.setEndTime(new Date());
                        jobRepository.update(stepExecution); // repository update
                    }
                }
                jobRepository.update(jobExecution);// repository update
                log.info("Updated job execution status : job id = {}", jobExecution.getJobId());
            }
        }
        log.info("stopped running jobs.");
    }
}

package chocoteamteam.togather.batch.schduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;

@Slf4j
@RequiredArgsConstructor
@Component
public class techStackStatisticsJobScheduler {
    private final JobLauncher jobLauncher;
    private final Job techStackStatisticsJob;

    // 월 실행, 지난 주 월 ~ 일 통계
    @Scheduled(cron = "0 30 0 ? * MON")
    public void runJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        LocalDate now = LocalDate.now();
        jobLauncher.run(techStackStatisticsJob, new JobParametersBuilder()
                .addString("startDate", LocalDateTime.of(now.minusDays(7), LocalTime.MIN).toString())
                .addString("endDate", LocalDateTime.of(now.minusDays(1), LocalTime.MAX).toString())
                .addLong("weeks", (long) (now.get(WeekFields.ISO.weekOfYear()) - 1))
                .toJobParameters());

    }
}

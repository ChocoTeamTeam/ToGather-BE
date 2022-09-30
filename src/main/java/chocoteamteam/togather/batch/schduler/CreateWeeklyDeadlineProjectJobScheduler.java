package chocoteamteam.togather.batch.schduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CreateWeeklyDeadlineProjectJobScheduler {

    private final Job createWeeklyDeadlineProjectJob;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 0 2 * * 1")
    public void runCreateWeeklyDeadlineProjectJob() throws Exception {
        jobLauncher.run(createWeeklyDeadlineProjectJob, new JobParametersBuilder()
                .addString("fromDate", LocalDate.now().toString())
                .toJobParameters());
    }
}
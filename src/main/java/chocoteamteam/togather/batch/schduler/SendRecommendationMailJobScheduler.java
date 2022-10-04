package chocoteamteam.togather.batch.schduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class SendRecommendationMailJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job sendRecommendationMailJob;

    //매주 월요일 7시 메일 발송
    @Scheduled(cron = " 0 0 7 ? * MON")
    public void runSendRecommendationMailJob() throws Exception {
        jobLauncher.run(sendRecommendationMailJob, new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters());
    }
}

package chocoteamteam.togather.batch.schduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class CreateRecommendationProjectJobScheduler {

    private final Job createRecommendationProjectJob;
    private final JobLauncher jobLauncher;

    // 매주 월요일 2시 이번 주 마감 되는 추천 공고 저장
    @Scheduled(cron = "0 0 2 ? * MON")
    public void runCreateRecommendationProjectJob() throws Exception {

        LocalDate now = LocalDate.now();
        jobLauncher.run(createRecommendationProjectJob, new JobParametersBuilder()
                .addString("startDate", now.toString())
                .addString("endDate", now.plusDays(6).toString())
                .toJobParameters());
    }
}

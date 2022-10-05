package chocoteamteam.togather.batch.application.job;

import chocoteamteam.togather.batch.BatchTestConfig;
import chocoteamteam.togather.batch.application.job.listener.JobExecutionLogger;
import chocoteamteam.togather.batch.application.service.SendMailService;
import chocoteamteam.togather.repository.impl.QueryDslTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SendRecommendationMailJobConfig.class,
        BatchTestConfig.class,
        QueryDslTestConfig.class,
        JobExecutionLogger.class
})
@Import(SendMailService.class)
class SendRecommendationMailJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private SendMailService sendMailService;

    @Test
    @DisplayName("주간 추천 공고 메일 전송")
    void SendRecommendationProjectJob_SUCCESS() throws Exception {

        given(sendMailService.sendMail(any())).willReturn(true);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters());

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}
package chocoteamteam.togather.batch.application.job;

import chocoteamteam.togather.batch.application.service.SendMailService;
import chocoteamteam.togather.batch.domain.entity.Mail;
import chocoteamteam.togather.batch.domain.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SendRecommendationMailJobConfig {
    private static final int CHUNK_SIZE = 10;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SendMailService sendMailService;
    private final MailRepository mailRepository;

    @Bean
    public Job sendRecommendationMailJob(JobExecutionListener jobExecutionListener) {
        return jobBuilderFactory.get("sendRecommendationMailJob")
                .start(sendRecommendationMailStep())
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    @JobScope
    public Step sendRecommendationMailStep() {
        return stepBuilderFactory.get("sendRecommendationMailStep")
                .<Mail, Mail>chunk(CHUNK_SIZE)
                .reader(sendRecommendationMailReader())
                .writer(sendRecommendationMailWriter(sendMailService))
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Mail> sendRecommendationMailReader() {
        return new RepositoryItemReaderBuilder<Mail>()
                .name("sendRecommendationMailReader")
                .repository(mailRepository)
                .methodName("findAll")
                .pageSize(CHUNK_SIZE)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Mail> sendRecommendationMailWriter(SendMailService sendMailService) {
        return mails -> mails.forEach(sendMailService::sendMail);
    }
}

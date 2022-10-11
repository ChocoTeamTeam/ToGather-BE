package chocoteamteam.togather.batch.application.job;

import chocoteamteam.togather.batch.application.job.param.RecommendationProjectJobParam;
import chocoteamteam.togather.batch.application.model.SimpleMemberTechStackInfoDto;
import chocoteamteam.togather.batch.application.service.CreateRecommendationProjectService;
import chocoteamteam.togather.batch.domain.entity.Mail;
import chocoteamteam.togather.batch.domain.repository.MailRepository;
import chocoteamteam.togather.type.MemberStatus;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.querydsl.reader.QuerydslPagingItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static chocoteamteam.togather.entity.QMember.member;
import static chocoteamteam.togather.entity.QMemberTechStack.memberTechStack;

@Configuration
@RequiredArgsConstructor
public class CreateRecommendationProjectJobConfig {

    private static final int CHUNK_SIZE = 10;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final RecommendationProjectJobParam jobParam;
    private final MailRepository mailRepository;

    private final CreateRecommendationProjectService createRecommendationProjectService;

    @Bean
    public Job createRecommendationProjectJob(JobExecutionListener jobExecutionListener) {
        return jobBuilderFactory.get("createRecommendationProjectJob")
                .start(deleteMailStep())
                .next(createRecommendationProjectStep())
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    @JobScope
    public Step deleteMailStep() {
        return stepBuilderFactory.get("deleteMailStep")
                .tasklet((contribution, chunkContext) -> {
                    mailRepository.deleteAll();
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    @JobScope
    public Step createRecommendationProjectStep() {
        return stepBuilderFactory.get("createRecommendationProjectStep")
                .<SimpleMemberTechStackInfoDto, Mail>chunk(CHUNK_SIZE)
                .reader(createWeeklyDeadlineProjectReader())
                .processor(createWeeklyDeadlineProjectProcessor(createRecommendationProjectService))
                .writer(createWeeklyDeadlineProjectWriter())
                .build();
    }


    @Bean
    @StepScope
    public QuerydslPagingItemReader<SimpleMemberTechStackInfoDto> createWeeklyDeadlineProjectReader() {
        return new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, queryFactory -> queryFactory
                .select(Projections.constructor(SimpleMemberTechStackInfoDto.class,
                        member
                ))
                .from(member)
                .where(member.status.eq(MemberStatus.PERMITTED))
                .innerJoin(member.memberTechStacks, memberTechStack)
                .distinct()
        );
    }


    @Bean
    @StepScope
    public ItemProcessor<SimpleMemberTechStackInfoDto, Mail> createWeeklyDeadlineProjectProcessor(
            CreateRecommendationProjectService createRecommendationProjectService
    ) {
        return member -> createRecommendationProjectService.getMatchedProjectAndConvertMail(
                member,
                jobParam.getStartDate(),
                jobParam.getEndDate()
        );
    }

    @Bean
    @StepScope
    public JpaItemWriter<Mail> createWeeklyDeadlineProjectWriter() {
        return new JpaItemWriterBuilder<Mail>()
                .entityManagerFactory(emf)
                .build();
    }
}

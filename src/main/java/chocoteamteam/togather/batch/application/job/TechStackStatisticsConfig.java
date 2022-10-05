package chocoteamteam.togather.batch.application.job;


import chocoteamteam.togather.batch.application.model.TechStatisticsDto;
import chocoteamteam.togather.batch.application.job.param.TechStatisticsJobParam;
import chocoteamteam.togather.batch.domain.entity.WeeklyTechStatistics;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.querydsl.reader.QuerydslPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static chocoteamteam.togather.entity.QProject.project;
import static chocoteamteam.togather.entity.QProjectTechStack.projectTechStack;
import static chocoteamteam.togather.entity.QTechStack.techStack;

@RequiredArgsConstructor
@Configuration
public class TechStackStatisticsConfig {
    private static final int CHUNK_SIZE = 10;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TechStatisticsJobParam jobParam;
    private final EntityManagerFactory emf;

    @Bean
    @Qualifier("techStackStatisticsJob")
    public Job techStackStatisticsJob(JobExecutionListener jobExecutionListener) {
        return jobBuilderFactory.get("techStackStatisticsJob")
                .incrementer(new RunIdIncrementer())
                .start(techStackStatisticsStep())
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    @JobScope
    public Step techStackStatisticsStep() {
        return stepBuilderFactory
                .get("techStackStatisticsStep")
                .<TechStatisticsDto, WeeklyTechStatistics>chunk(CHUNK_SIZE)
                .reader(techStackStatisticsReader())
                .processor(techStackStatisticsProcessor())
                .writer(techStackStatisticsWriter())
                .build();
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<TechStatisticsDto> techStackStatisticsReader() {
        return new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, queryFactory -> queryFactory
                .select(Projections.constructor(TechStatisticsDto.class,
                        techStack.id,
                        techStack.name,
                        techStack.category,
                        techStack.id.count()))
                .from(projectTechStack)
                .innerJoin(projectTechStack.project, project)
                .innerJoin(projectTechStack.techStack, techStack)
                .where(project.createdAt.between(jobParam.getStartDate(), jobParam.getEndDate()))
                .groupBy(techStack.id)
        );
    }

    @Bean
    @StepScope
    public ItemProcessor<TechStatisticsDto, WeeklyTechStatistics> techStackStatisticsProcessor() {
        return (techStatisticsDto) -> WeeklyTechStatistics.builder()
                .techStackId(techStatisticsDto.getTechStackId())
                .techStackName(techStatisticsDto.getTechStackName())
                .techStackCategory(techStatisticsDto.getTechStackCategory())
                .count(techStatisticsDto.getCount())
                .weeks(jobParam.getWeeks())
                .build();
    }

    @Bean
    @StepScope
    public JpaItemWriter<WeeklyTechStatistics> techStackStatisticsWriter() {
        return new JpaItemWriterBuilder<WeeklyTechStatistics>()
                .entityManagerFactory(emf)
                .build();
    }

}

package chocoteamteam.togather.batch.application.job;

import chocoteamteam.togather.batch.application.job.param.WeeklyDeadlineParam;
import chocoteamteam.togather.batch.domain.entity.WeeklyDeadlineProject;
import chocoteamteam.togather.entity.ProjectTechStack;
import chocoteamteam.togather.type.ProjectStatus;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static chocoteamteam.togather.entity.QProject.project;
import static chocoteamteam.togather.entity.QProjectTechStack.projectTechStack;
import static chocoteamteam.togather.entity.QTechStack.techStack;


@Configuration
@RequiredArgsConstructor
public class CreateWeeklyDeadlineProjectJobConfig {

    private static final int CHUNK_SIZE = 10;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WeeklyDeadlineParam weeklyDeadlineParam;
    private final EntityManagerFactory emf;

    @Bean
    public Job createWeeklyDeadlineProjectJob(JobExecutionListener jobExecutionListener) {
        return jobBuilderFactory.get("createWeeklyDeadlineProjectJob")
                .start(createWeeklyDeadlineProjectStep())
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    @JobScope
    public Step createWeeklyDeadlineProjectStep() {
        return stepBuilderFactory.get("createWeeklyDeadlineProjectStep")
                .<ProjectTechStack, WeeklyDeadlineProject>chunk(CHUNK_SIZE)
                .reader(createWeeklyDeadlineProjectReader())
                .processor(createWeeklyDeadlineProjectProcessor())
                .writer(createWeeklyDeadlineProjectWriter())
                .build();
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<ProjectTechStack> createWeeklyDeadlineProjectReader() {
        return new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, queryFactory -> queryFactory
                .selectFrom(projectTechStack)
                .innerJoin(projectTechStack.project, project).fetchJoin()
                .innerJoin(projectTechStack.techStack, techStack).fetchJoin()
                .where(project.status.eq(ProjectStatus.RECRUITING),
                        (project.deadline.between(weeklyDeadlineParam.getFromDate(),
                                weeklyDeadlineParam.getToDate())),
                        (project.createdAt.before(LocalDateTime.of(weeklyDeadlineParam.getFromDate(), LocalTime.MIN))))
        );
    }

    public ItemProcessor<ProjectTechStack, WeeklyDeadlineProject> createWeeklyDeadlineProjectProcessor() {
        return pt -> WeeklyDeadlineProject.builder()
                .projectId(pt.getProject().getId())
                .projectName(pt.getProject().getTitle())
                .techStackId(pt.getTechStack().getId())
                .techStackName(pt.getTechStack().getName())
                .deadline(pt.getProject().getDeadline())
                .build();
    }

    public JpaItemWriter<WeeklyDeadlineProject> createWeeklyDeadlineProjectWriter() {
        return new JpaItemWriterBuilder<WeeklyDeadlineProject>()
                .entityManagerFactory(emf)
                .build();
    }
}
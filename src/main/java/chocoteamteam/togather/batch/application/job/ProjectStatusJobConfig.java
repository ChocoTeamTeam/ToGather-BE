package chocoteamteam.togather.batch.application.job;

import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.type.ProjectStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProjectStatusJobConfig {
    private static final int CHUNK_SIZE = 10;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ProjectRepository projectRepository;

    @Bean
    public Job changeProjectStatusJob(JobExecutionListener jobExecutionListener) {
        return jobBuilderFactory.get("changeProjectStatusJob")
                .incrementer(new RunIdIncrementer())
                .start(changeProjectStatusStep())
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    @JobScope
    public Step changeProjectStatusStep() {
        return stepBuilderFactory.get("changeProjectStatusStep")
                .<Project, Project>chunk(CHUNK_SIZE)
                .reader(changeProjectStatusReader())
                .processor(changeProjectStatusProcessor())
                .writer(changeProjectStatusWriter())
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Project> changeProjectStatusReader() {
        LocalDate now = LocalDate.now();
        return new RepositoryItemReaderBuilder<Project>()
                .name("changeProjectStatusReader")
                .repository(projectRepository)
                .methodName("findAllByStatusAndDeadlineBefore")
                .arguments(ProjectStatus.RECRUITING, now)
                .pageSize(CHUNK_SIZE)
                .sorts(Map.of("id", Sort.DEFAULT_DIRECTION))
                .build();
    }

    public ItemProcessor<Project, Project> changeProjectStatusProcessor() {
        return project -> {
            project.setStatus(ProjectStatus.COMPLETED);
            return project;
        };
    }

    public RepositoryItemWriter<Project> changeProjectStatusWriter() {
        return new RepositoryItemWriterBuilder<Project>()
                .repository(projectRepository)
                .build();
    }
}
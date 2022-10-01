package chocoteamteam.togather.batch.application.job;

import chocoteamteam.togather.batch.BatchTestConfig;
import chocoteamteam.togather.batch.application.job.listener.JobExecutionLogger;
import chocoteamteam.togather.batch.application.job.param.TechStatisticsJobParam;
import chocoteamteam.togather.batch.domain.entity.WeeklyTechStatistics;
import chocoteamteam.togather.batch.domain.repository.WeeklyTechStatisticsRepository;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.entity.ProjectTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.repository.ProjectTechStackRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.repository.impl.QueryDslTestConfig;
import chocoteamteam.togather.type.TechCategory;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TechStackStatisticsConfig.class, BatchTestConfig.class, QueryDslTestConfig.class,
        JobExecutionLogger.class, TechStatisticsJobParam.class})
class TechStackStatisticsConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TechStackRepository techStackRepository;
    @Autowired
    private ProjectTechStackRepository projectTechStackRepository;
    @Autowired
    private WeeklyTechStatisticsRepository weeklyTechStatisticsRepository;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(Member.builder()
                .email("togather@to.com")
                .nickname("두개더")
                .profileImage("img_url")
                .build());
        Project project = projectRepository.save(Project.builder()
                .member(member)
                .build());
        Project project2 = projectRepository.save(Project.builder()
                .member(member)
                .build());
        TechStack techStack1 = techStackRepository.save(TechStack.builder()
                .name("tech1")
                .category(TechCategory.BACKEND)
                .build());
        TechStack techStack2 = techStackRepository.save(TechStack.builder()
                .name("tech2")
                .category(TechCategory.FRONTEND)
                .build());

        projectTechStackRepository.save(new ProjectTechStack(project, techStack1));
        projectTechStackRepository.save(new ProjectTechStack(project2, techStack1));
        projectTechStackRepository.save(new ProjectTechStack(project2, techStack2));
    }

    @Test
    @DisplayName("기술스택 주간 통계")
    void techStackStatisticsConfig_Test() throws Exception {
        //given
        //when
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addString("startDate", now.minusDays(7).toString())
                .addString("endDate", now.plusDays(1).toString()) // Add a day to test
                .addLong("weeks", (long) (now.get(WeekFields.ISO.weekOfYear()) - 1))
                .toJobParameters());

        //then
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        List<WeeklyTechStatistics> all = weeklyTechStatisticsRepository.findAll();
        assertEquals(2, all.size());
    }
}
package chocoteamteam.togather.batch.application.job;

import chocoteamteam.togather.batch.BatchTestConfig;
import chocoteamteam.togather.batch.application.job.listener.JobExecutionLogger;
import chocoteamteam.togather.batch.application.job.param.WeeklyDeadlineParam;
import chocoteamteam.togather.batch.domain.repository.WeeklyDeadlineProjectRepository;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.entity.ProjectTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.repository.ProjectTechStackRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.repository.impl.QueryDslTestConfig;
import chocoteamteam.togather.type.ProjectStatus;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CreateWeeklyDeadlineProjectJobConfig.class, WeeklyDeadlineParam.class, BatchTestConfig.class, QueryDslTestConfig.class, JobExecutionLogger.class})
class CreateWeeklyDeadlineProjectJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TechStackRepository techStackRepository;
    @Autowired
    private ProjectTechStackRepository projectTechStackRepository;
    @Autowired
    private WeeklyDeadlineProjectRepository weeklyDeadlineProjectRepository;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .email("togather@to.com")
                .nickname("두개더")
                .profileImage("img_url")
                .build();

        Project project = Project.builder()
                .member(member)
                .title("곧 모집 마감")
                .content("곧 모집 마감")
                .personnel(5)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location("서울")
                .deadline(LocalDate.now().plusDays(5))
                .build();

        TechStack techStack = TechStack.builder()
                .name("my_tech")
                .category(TechCategory.BACKEND)
                .image("my_tech_img")
                .build();

        ProjectTechStack projectTechStack = new ProjectTechStack(project, techStack);

        memberRepository.save(member);
        projectRepository.save(project);
        techStackRepository.save(techStack);
        projectTechStackRepository.save(projectTechStack);
    }

    @Test
    @DisplayName("모집 마감 기간이 7일 미만으로 남은 모집 공고 모음")
    public void CreateWeeklyDeadlineProjectJob() throws Exception {

        LocalDate today = LocalDate.now();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addString("fromDate", today.toString()).toJobParameters());

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(projectTechStackRepository.findAll().size()).isEqualTo(1);

        // 모든 데이터가 오늘 입력된 데이터라 '어제까지 등록된 공고' 조건에 의해 걸러짐
        assertThat(weeklyDeadlineProjectRepository.findAll().size()).isEqualTo(0);
    }
}
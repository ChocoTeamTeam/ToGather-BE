package chocoteamteam.togather.batch.application.job;

import chocoteamteam.togather.batch.BatchTestConfig;
import chocoteamteam.togather.batch.application.job.listener.JobExecutionLogger;
import chocoteamteam.togather.batch.application.job.param.ProjectStatusJobParam;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.repository.impl.QueryDslTestConfig;
import chocoteamteam.togather.type.ProjectStatus;
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

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProjectStatusJobConfig.class, BatchTestConfig.class, QueryDslTestConfig.class,
        JobExecutionLogger.class, ProjectStatusJobParam.class})
class ProjectStatusJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        Member member = Member.builder()
                .email("togather@to.com")
                .nickname("두개더")
                .profileImage("img_url")
                .build();

        projectRepository.save(Project.builder()
                .member(memberRepository.save(member))
                .title("제목999")
                .content("내용999")
                .personnel(5)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location("서울")
                .deadline(LocalDate.now().minusDays(2))
                .build());
    }

    @Test
    @DisplayName("마감일 지난 프로젝트 상태 변경 Job 테스트")
    public void projectStatusJobTest() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addString("nowDate", LocalDate.now().toString())
                .toJobParameters());

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(ProjectStatus.COMPLETED, projectRepository.findById(1L).get().getStatus());

    }
}
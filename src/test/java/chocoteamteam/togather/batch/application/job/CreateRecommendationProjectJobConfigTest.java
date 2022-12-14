package chocoteamteam.togather.batch.application.job;

import chocoteamteam.togather.batch.BatchTestConfig;
import chocoteamteam.togather.batch.application.job.listener.JobExecutionLogger;
import chocoteamteam.togather.batch.application.job.param.RecommendationProjectJobParam;
import chocoteamteam.togather.batch.application.model.MemberRecommendationProjectDto;
import chocoteamteam.togather.batch.application.service.CreateRecommendationProjectService;
import chocoteamteam.togather.batch.domain.entity.Mail;
import chocoteamteam.togather.batch.domain.repository.MailRepository;
import chocoteamteam.togather.entity.*;
import chocoteamteam.togather.repository.*;
import chocoteamteam.togather.repository.impl.QueryDslTestConfig;
import chocoteamteam.togather.type.MemberStatus;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CreateRecommendationProjectJobConfig.class, BatchTestConfig.class, QueryDslTestConfig.class,
        JobExecutionLogger.class, CreateRecommendationProjectService.class, RecommendationProjectJobParam.class})
class CreateRecommendationProjectJobConfigTest {

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
    private MemberTechStackRepository memberTechStackRepository;

    @Autowired
    private MailRepository mailRepository;

    @BeforeEach
    void beforeEach() {
        mailRepository.save(Mail.builder()
                .email("e@naver.com")
                .subject("?????? ??????")
                .content("?????? ??????")
                .build());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????? ?????? ?????? ??????")
    void createRecommendationProjectJob_SUCCESS() throws Exception {

        //given
        LocalDate now = LocalDate.now();
        Member author = Member.builder()
                .email("togather@to.com")
                .nickname("?????????")
                .profileImage("img_url")
                .status(MemberStatus.PERMITTED)
                .build();

        Member member = Member.builder()
                .email("to@gather.com")
                .nickname("??????")
                .profileImage("img_url")
                .status(MemberStatus.PERMITTED)
                .build();

        List<Member> members = new ArrayList<>();
        members.add(author);
        members.add(member);

        //??? ?????? ???????????? ????????????
        Project targetProject = Project.builder()
                .member(author)
                .title("??? ?????? ??????")
                .content("??? ?????? ??????")
                .personnel(5)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location(Location.builder()
                        .address("??????????????? ????????? ?????? ???????????? 231 ?????? ????????? 6??? 7???")
                        .latitude(37.503050)
                        .longitude(127.041583)
                        .build())
                .deadline(now.plusDays(5))
                .build();

        //?????? ?????? ????????? ???????????? ?????? ???????????? - ?????? ?????? ?????? ??????
        Project project1 = Project.builder()
                .member(author)
                .title("?????? ??????")
                .content("?????? ??????")
                .personnel(3)
                .status(ProjectStatus.COMPLETED)
                .offline(false)
                .location(Location.builder()
                        .address("??????????????? ????????? ?????? ???????????? 231 ?????? ????????? 6??? 7???")
                        .latitude(37.503050)
                        .longitude(127.041583)
                        .build())
                .deadline(now.plusDays(5))
                .build();

        //?????? ?????? ????????? ???????????? ?????? ???????????? - ???????????? ????????? ?????? ??????
        Project project2 = Project.builder()
                .member(author)
                .title("?????? ??????")
                .content("?????? ??????")
                .personnel(3)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location(Location.builder()
                        .address("??????????????? ????????? ?????? ???????????? 231 ?????? ????????? 6??? 7???")
                        .latitude(37.503050)
                        .longitude(127.041583)
                        .build())
                .deadline(now.plusDays(7))
                .build();

        //????????? ??????????????? ????????? ?????? ????????????
        Project project3 = Project.builder()
                .member(author)
                .title("?????? ??????")
                .content("?????? ??????")
                .personnel(3)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location(Location.builder()
                        .address("??????????????? ????????? ?????? ???????????? 231 ?????? ????????? 6??? 7???")
                        .latitude(37.503050)
                        .longitude(127.041583)
                        .build())
                .deadline(now.plusDays(4))
                .build();

        List<Project> projects = new ArrayList<>();
        projects.add(targetProject);
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);

        //??????????????? ????????? ?????? ?????? ??? ?????? ??????
        TechStack techStack = TechStack.builder()
                .name("my_tech")
                .category(TechCategory.BACKEND)
                .image("my_tech_img")
                .build();

        //????????? ?????? ?????? ?????? ??????????????? ?????? ?????? ?????? ??????
        TechStack techStack1 = TechStack.builder()
                .name("project_only")
                .category(TechCategory.BACKEND)
                .image("img")
                .build();

        List<TechStack> techStacks = new ArrayList<>();
        techStacks.add(techStack);
        techStacks.add(techStack1);

        ProjectTechStack projectTechStack = new ProjectTechStack(targetProject, techStack);
        ProjectTechStack projectTechStack1 = new ProjectTechStack(project1, techStack);
        ProjectTechStack projectTechStack2 = new ProjectTechStack(project2, techStack);
        ProjectTechStack projectTechStack3 = new ProjectTechStack(project3, techStack1);

        List<ProjectTechStack> projectTechStacks = new ArrayList<>();
        projectTechStacks.add(projectTechStack);
        projectTechStacks.add(projectTechStack1);
        projectTechStacks.add(projectTechStack2);
        projectTechStacks.add(projectTechStack3);

        MemberTechStack memberTechStack = MemberTechStack.builder()
                .member(author)
                .techStack(techStack)
                .build();

        memberRepository.saveAll(members);
        projectRepository.saveAll(projects);
        techStackRepository.saveAll(techStacks);
        projectTechStackRepository.saveAll(projectTechStacks);
        memberTechStackRepository.save(memberTechStack);


        //when
        //?????? ???????????? ????????? ?????? ????????? ????????? ????????? ?????? ?????? ?????? ?????????????????? '??????'????????? '??????'??? ??????
        LocalDate startDate = now.plusDays(1);
        LocalDate endDate = now.plusDays(6);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder()

                .addString("startDate", startDate.toString())
                .addString("endDate", endDate.toString())
                .toJobParameters());

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        List<Mail> mails = mailRepository.findAll();
        assertThat(mails).hasSize(1);
        List<MemberRecommendationProjectDto> targetProjects =
                projectRepository.findAllByTechStackIdsAndDeadline(
                        List.of(techStack.getId()),
                        startDate,
                        endDate,
                        member.getId()
                );
        assertThat(targetProjects).hasSize(1);
        assertThat(targetProjects.get(0).getSubject()).isEqualTo(targetProject.getTitle());
    }

    @Test
    @DisplayName("?????? Mail ?????? ????????? Step ?????????")
    void deleteMailInfo() {
        List<Mail> mails = mailRepository.findAll();
        assertThat(mails).hasSize(1);

        JobExecution jobExecution = jobLauncherTestUtils.launchStep("deleteMailStep");
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        mails = mailRepository.findAll();
        assertThat(mails).hasSize(0);
    }
}
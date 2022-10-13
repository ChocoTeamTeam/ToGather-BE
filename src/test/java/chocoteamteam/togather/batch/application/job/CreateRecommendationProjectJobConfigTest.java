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
                .subject("지난 제목")
                .content("지난 내용")
                .build());
    }

    @Test
    @DisplayName("유저별 마감 임박 공고 추천 정보 저장")
    void createRecommendationProjectJob_SUCCESS() throws Exception {

        //given
        LocalDate now = LocalDate.now();
        Member author = Member.builder()
                .email("togather@to.com")
                .nickname("작성자")
                .profileImage("img_url")
                .status(MemberStatus.PERMITTED)
                .build();

        Member member = Member.builder()
                .email("to@gather.com")
                .nickname("대상")
                .profileImage("img_url")
                .status(MemberStatus.PERMITTED)
                .build();

        List<Member> members = new ArrayList<>();
        members.add(author);
        members.add(member);

        //곧 모집 마감되는 프로젝트
        Project targetProject = Project.builder()
                .member(author)
                .title("곧 모집 마감")
                .content("곧 모집 마감")
                .personnel(5)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location(Location.builder()
                        .address("서울특별시 강남구 센터 테헤란로 231 필드 웨스트 6층 7층")
                        .latitude(37.503050)
                        .longitude(127.041583)
                        .build())
                .deadline(now.plusDays(5))
                .build();

        //모집 마감 임박에 포함되지 않는 프로젝트 - 이미 모집 상태 마감
        Project project1 = Project.builder()
                .member(author)
                .title("해당 아님")
                .content("해당 아님")
                .personnel(3)
                .status(ProjectStatus.COMPLETED)
                .offline(false)
                .location(Location.builder()
                        .address("서울특별시 강남구 센터 테헤란로 231 필드 웨스트 6층 7층")
                        .latitude(37.503050)
                        .longitude(127.041583)
                        .build())
                .deadline(now.plusDays(5))
                .build();

        //모집 마감 임박에 포함되지 않는 프로젝트 - 마감일이 일주일 넘게 남음
        Project project2 = Project.builder()
                .member(author)
                .title("해당 아님")
                .content("해당 아님")
                .personnel(3)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location(Location.builder()
                        .address("서울특별시 강남구 센터 테헤란로 231 필드 웨스트 6층 7층")
                        .latitude(37.503050)
                        .longitude(127.041583)
                        .build())
                .deadline(now.plusDays(7))
                .build();

        //회원의 기술스택과 겹치지 않는 프로젝트
        Project project3 = Project.builder()
                .member(author)
                .title("해당 아님")
                .content("해당 아님")
                .personnel(3)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location(Location.builder()
                        .address("서울특별시 강남구 센터 테헤란로 231 필드 웨스트 6층 7층")
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

        //프로젝트와 회원이 같이 갖게 될 기술 스택
        TechStack techStack = TechStack.builder()
                .name("my_tech")
                .category(TechCategory.BACKEND)
                .image("my_tech_img")
                .build();

        //회원은 갖고 있지 않고 프로젝트만 갖고 있을 기술 스택
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
        //오늘 날짜로만 생성이 돼서 생성일 조건에 걸리지 않게 하기 위해 테스트에서만 '오늘'날짜를 '내일'로 설정
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
    @DisplayName("지난 Mail 정보 지우기 Step 테스트")
    void deleteMailInfo() {
        List<Mail> mails = mailRepository.findAll();
        assertThat(mails).hasSize(1);

        JobExecution jobExecution = jobLauncherTestUtils.launchStep("deleteMailStep");
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        mails = mailRepository.findAll();
        assertThat(mails).hasSize(0);
    }
}
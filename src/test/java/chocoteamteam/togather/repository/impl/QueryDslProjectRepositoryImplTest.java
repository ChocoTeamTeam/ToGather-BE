package chocoteamteam.togather.repository.impl;

import chocoteamteam.togather.DataCleanUp;
import chocoteamteam.togather.dto.ProjectCondition;
import chocoteamteam.togather.dto.queryDslSimpleDto.SimpleProjectDto;
import chocoteamteam.togather.entity.*;
import chocoteamteam.togather.repository.*;
import chocoteamteam.togather.type.ProjectStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({QueryDslTestConfig.class, DataCleanUp.class})
@DataJpaTest
@ExtendWith(SpringExtension.class)
class QueryDslProjectRepositoryImplTest {
    @Autowired
    private QueryDslProjectRepositoryImpl queryDslProjectRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectTechStackRepository projectTechStackRepository;

    @Autowired
    private TechStackRepository techStackRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private DataCleanUp dataCleanUp;

    @BeforeAll
    public void dataSetup() {
        dataCleanUp.execute();

        Member aMember = memberRepository.save(Member.builder().email("www.a.com").nickname("aaaa name").profileImage("image").build());
        Member bMember = memberRepository.save(Member.builder().email("www.b.com").nickname("bbbb name").profileImage("image").build());
        Member cMember = memberRepository.save(Member.builder().email("www.c.com").nickname("cccc name").profileImage("image").build());

        TechStack react = techStackRepository.save(TechStack.builder().name("react").build());
        TechStack angular = techStackRepository.save(TechStack.builder().name("angular").build());
        TechStack spring = techStackRepository.save(TechStack.builder().name("spring").build());
        TechStack nodejs = techStackRepository.save(TechStack.builder().name("nodejs").build());
        TechStack php = techStackRepository.save(TechStack.builder().name("php").build());

        Project aProject = projectRepository.save(Project.builder().member(aMember)
                .title("aaaa title").content("aaaa content").status(ProjectStatus.RECRUITING).build());
        Project bProject = projectRepository.save(Project.builder().member(bMember)
                .title("bbbb title").content("bbbb content").status(ProjectStatus.RECRUITING).build());
        Project cProject = projectRepository.save(Project.builder().member(cMember)
                .title("cccc title").content("cccc content").status(ProjectStatus.COMPLETED).build());
        Project dProject = projectRepository.save(Project.builder().member(aMember)
                .title("dddd title").content("dddd content").status(ProjectStatus.COMPLETED).build());


        projectTechStackRepository.save(new ProjectTechStack(aProject, react))
                .setProject(aProject);
        projectTechStackRepository.save(new ProjectTechStack(aProject, spring))
                .setProject(aProject);
        projectTechStackRepository.save(new ProjectTechStack(aProject, nodejs))
                .setProject(aProject);

        projectTechStackRepository.save(new ProjectTechStack(bProject, react))
                .setProject(bProject);
        projectTechStackRepository.save(new ProjectTechStack(bProject, spring))
                .setProject(bProject);
        projectTechStackRepository.save(new ProjectTechStack(bProject, nodejs))
                .setProject(bProject);

        projectTechStackRepository.save(new ProjectTechStack(cProject, php))
                .setProject(cProject);
        projectTechStackRepository.save(new ProjectTechStack(cProject, react))
                .setProject(cProject);

        projectTechStackRepository.save(new ProjectTechStack(dProject, php))
                .setProject(dProject);
        projectTechStackRepository.save(new ProjectTechStack(dProject, angular))
                .setProject(dProject);

        projectRepository.save(aProject);
        projectRepository.save(bProject);
        projectRepository.save(cProject);
        projectRepository.save(dProject);

        System.out.println("-------------------- insert query close ----------------\n\n");
    }

    private final long TOTAL_DATA_SIZE = 4;

    @Test
    @Order(1)
    void data_Setup_time() {
        queryDslProjectRepository.findAllOptionAndSearch(ProjectCondition.builder()
                .limit(1L)
                .build());

        System.out.println("insert Data loading------------");
    }

    @Test
    @DisplayName("프로젝트 상세 조회")
    void findByIdQueryTest() {
        //given
        //when
        Project project = projectRepository.findByIdWithMemberAndTechStack(1L).get();

        //then
        assertEquals(1L, project.getId());
        assertEquals("aaaa name", project.getMember().getNickname());
        assertEquals("aaaa title", project.getTitle());
        assertEquals(3, project.getProjectTechStacks().size());
    }

    @Test
    @DisplayName("아무 조건 없이 조회")
    void noOption_search_test() {
        //given
        ProjectCondition projectCondition = ProjectCondition.builder()
                .limit(10L)
                .build();
        //when
        List<SimpleProjectDto> result = queryDslProjectRepository.findAllOptionAndSearch(projectCondition);
        //then

        assertEquals(4, result.size());
    }

    @Test
    @DisplayName("내가 올린 프로젝트 조회")
    void find_my_project() {
        //given
        //when
        List<SimpleProjectDto> result = queryDslProjectRepository.findAllByMemberId(1L);
        //then

        assertTrue(result.size() > 0);
        for (SimpleProjectDto simpleProjectDto : result) {
            assertEquals(1L, simpleProjectDto.getMember().getId());
        }
    }

    @Test
    @DisplayName("내가 참여중인 프로젝트 조회")
    void find_my_participating_project() {
        //given
        projectMemberRepository.save(ProjectMember.builder()
                .project(projectRepository.findById(1L).get())
                .member(memberRepository.findById(1L).get()).build());

        //when
        List<ProjectMember> result = queryDslProjectRepository.findAllByProjectMemberId(1L);
        //then

        assertTrue(result.size() > 0);
        for (ProjectMember projectMember : result) {
            assertEquals(1L, projectMember.getMember().getId());
        }
    }

    @Test
    @DisplayName("조회 페이징 테스트")
    void noOption_paging_test() {
        //given
        long pageSize = 2L;
        int pageNumber = 1;

        ProjectCondition projectCondition = ProjectCondition.builder()
                .limit(pageSize)
                .pageNumber(pageNumber)
                .build();
        //when
        List<SimpleProjectDto> result = queryDslProjectRepository.findAllOptionAndSearch(projectCondition);
        //then

        assertEquals(2, result.size());

        // 0 page = [4L, 3L] , 1 page = [2L, 1L]
        assertEquals(2L, result.get(0).getId());
        assertEquals(1L, result.get(1).getId());
    }

    @Test
    @DisplayName("작성자 이름으로 검색")
    void author_search() {
        //given
        ProjectCondition projectCondition = ProjectCondition.builder()
                .limit(TOTAL_DATA_SIZE)
                .author("cc")
                .build();
        //when
        List<SimpleProjectDto> result = queryDslProjectRepository.findAllOptionAndSearch(projectCondition);
        //then

        assertTrue(result.size() > 0);
        for (SimpleProjectDto simpleProjectDto : result) {
            assertTrue(simpleProjectDto.getMember().getNickname().contains("cc"));
        }
    }

    @Test
    @DisplayName("제목으로 검색")
    void title_search() {
        //given
        ProjectCondition projectCondition = ProjectCondition.builder()
                .limit(TOTAL_DATA_SIZE)
                .title("dd")
                .build();
        //when
        List<SimpleProjectDto> result = queryDslProjectRepository.findAllOptionAndSearch(projectCondition);
        //then

        assertTrue(result.size() > 0);
        for (SimpleProjectDto simpleProjectDto : result) {
            assertTrue(simpleProjectDto.getTitle().contains("dd"));
        }
    }

    @Test
    @DisplayName("내용으로 검색")
    void content_search() {
        //given
        ProjectCondition projectCondition = ProjectCondition.builder()
                .limit(TOTAL_DATA_SIZE)
                .content("cccc")
                .build();
        //when
        List<SimpleProjectDto> result = queryDslProjectRepository.findAllOptionAndSearch(projectCondition);
        //then

        assertEquals(1, result.size());
    }


    @Test
    @DisplayName("프로젝트 상태로 필터링")
    void search_projectStatus() {
        //given
        ProjectCondition projectCondition = ProjectCondition.builder()
                .limit(TOTAL_DATA_SIZE)
                .projectStatus(ProjectStatus.RECRUITING)
                .build();
        //when
        List<SimpleProjectDto> result = queryDslProjectRepository.findAllOptionAndSearch(projectCondition);
        //then

        assertTrue(result.size() > 0);
        for (SimpleProjectDto simpleProjectDto : result) {
            assertEquals(ProjectStatus.RECRUITING, simpleProjectDto.getStatus());
        }
    }

    @Test
    @DisplayName("기술스택으로 조회")
    void search_skillStack() {
        //given
        TechStack react = techStackRepository.findById(1L).get();
        TechStack spring = techStackRepository.findById(3L).get();

        ProjectCondition projectCondition = ProjectCondition.builder()
                .limit(TOTAL_DATA_SIZE)
                .techStackIds(List.of(react.getId(), spring.getId()))
                .build();
        //when

        List<SimpleProjectDto> result = queryDslProjectRepository.findAllOptionAndSearch(projectCondition);
        //then

        assertEquals(3, result.size());

        for (SimpleProjectDto simpleProjectDto : result) {
            assertTrue(simpleProjectDto.getTechStacks().stream()
                    .anyMatch(techStack ->
                            techStack.getId().equals(react.getId()) ||
                                    techStack.getId().equals(spring.getId())));
        }
    }

    @Test
    @DisplayName("모든 조건 동시에 적용 조회 테스트")
    void all_condition_search() {
        //given
        TechStack react = techStackRepository.findById(1L).get();
        TechStack php = techStackRepository.findById(5L).get();

        ProjectCondition projectCondition = ProjectCondition.builder()
                .limit(TOTAL_DATA_SIZE)
                .techStackIds(List.of(react.getId(), php.getId()))
                .projectStatus(ProjectStatus.COMPLETED)
                .title("cccc")
                .build();
        //when

        List<SimpleProjectDto> result = queryDslProjectRepository.findAllOptionAndSearch(projectCondition);
        //then

        assertEquals(3, result.get(0).getId());
    }

}
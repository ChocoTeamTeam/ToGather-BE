package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CreateProjectForm;
import chocoteamteam.togather.dto.ProjectDetails;
import chocoteamteam.togather.dto.ProjectDto;
import chocoteamteam.togather.dto.UpdateProjectForm;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.entity.ProjectTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.ProjectException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.repository.ProjectTechStackRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.ProjectStatus;
import chocoteamteam.togather.type.TechCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TechStackRepository techStackRepository;
    @Mock
    private ProjectTechStackRepository projectTechStackRepository;
    @InjectMocks
    private ProjectService projectService;

    private Member member;
    private Project project;
    private final List<TechStack> techStacks = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        member = Member.builder()
                .id(9L)
                .email("togather@to.com")
                .nickname("두개더")
                .profileImage("img_url")
                .build();


        project = Project.builder()
                .id(999L)
                .member(member)
                .title("제목999")
                .content("내용999")
                .personnel(10)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location("서울")
                .deadline(LocalDate.of(2022, 9, 12))
                .build();

        for (int i = 0; i < 2; i++) {
            techStacks.add(TechStack.builder()
                    .id((long) +1)
                    .name("java" + i)
                    .category(TechCategory.BACKEND)
                    .image("img_url" + i)
                    .build());
        }
    }

    @Test
    @DisplayName("프로젝트 등록 성공")
    void createProjectSuccess() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        given(projectRepository.save(any()))
                .willReturn(project);

        given(techStackRepository.findAllById(any()))
                .willReturn(techStacks);

        //when
        CreateProjectForm form = new CreateProjectForm(
                "의미 없는 제목",
                "의미 없는 내용",
                1000,
                ProjectStatus.RECRUITING,
                false,
                "의미 없는 위치",
                LocalDate.of(2050, 9, 13),
                List.of(1000L, 1001L)
        );
        ProjectDto projectDto = projectService.createProject(8L, form);

        //then
        assertEquals(project.getId(), projectDto.getId());
        assertEquals(member.getId(), projectDto.getMember().getId());
        assertEquals(project.getTitle(), projectDto.getTitle());
        assertEquals(project.getContent(), projectDto.getContent());
        assertEquals(project.getPersonnel(), projectDto.getPersonnel());
        assertEquals(project.getStatus(), projectDto.getStatus());
        assertEquals(project.getLocation(), projectDto.getLocation());
        assertEquals(project.getDeadline(), projectDto.getDeadline());
        assertEquals(project.getId(), projectDto.getProjectTechStacks().get(1).getProjectId());
        assertEquals(techStacks.get(0).getName(), projectDto.getProjectTechStacks().get(0).getTechStack().getName());
        assertEquals(project.getProjectTechStacks().size(), projectDto.getProjectTechStacks().size());
        verify(projectTechStackRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("프로젝트 등록 실패 - 해당 멤버 없음")
    void createProject_NotFoundMember() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.createProject(1L, new CreateProjectForm()));
        //then
        assertEquals(ErrorCode.NOT_FOUND_MEMBER, exception.getErrorCode());
    }

    @Test
    @DisplayName("프로젝트 등록 실패 - 해당 기술 스택 없음")
    void createProject_NotFoundTechStack() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        given(techStackRepository.findAllById(any()))
                .willReturn(new ArrayList<>());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.createProject(1L, new CreateProjectForm(
                        "제목888",
                        "내용888",
                        20,
                        ProjectStatus.RECRUITING,
                        false,
                        "부산",
                        LocalDate.of(2022, 9, 13),
                        List.of(5L, 6L)
                )));
        //then
        assertEquals(ErrorCode.NOT_FOUND_TECH_STACK, exception.getErrorCode());
    }

    @Test
    @DisplayName("프로젝트 수정 성공")
    void updateProjectSuccess() {
        //given
        Project testProject = Project.builder().id(999L).member(member).build();
        TechStack techStack1 = TechStack.builder().id(1L).build();
        TechStack techStack2 = TechStack.builder().id(2L).build();
        TechStack techStack3 = TechStack.builder().id(3L).build();
        TechStack techStack4 = TechStack.builder().id(4L).build();
        TechStack techStack5 = TechStack.builder().id(5L).build();
        TechStack techStack6 = TechStack.builder().id(6L).build();

        ProjectTechStack projectTechStack1 = new ProjectTechStack(testProject, techStack1);
        projectTechStack1.setId(1L);
        ProjectTechStack projectTechStack2 = new ProjectTechStack(testProject, techStack2);
        projectTechStack2.setId(2L);
        ProjectTechStack projectTechStack3 = new ProjectTechStack(testProject, techStack3);
        projectTechStack3.setId(3L);


        //기존 기술 스택
        List<Long> prevTech = new ArrayList<>(List.of(1L, 2L, 3L));
        //새로 입력받은 기술 스택
        List<Long> newTech = new ArrayList<>(List.of(3L, 4L, 5L, 6L));
        //삭제 되어야할 기술 스택들
        List<ProjectTechStack> deleteList = List.of(projectTechStack1, projectTechStack2);

        given(projectRepository.findById(anyLong()))
                .willReturn(Optional.of(testProject));

        given(projectTechStackRepository.findTechStackIdsByProjectId(anyLong()))
                .willReturn(prevTech);

        given(projectTechStackRepository.deleteAllByIdIn(any()))
                .willReturn(deleteList);

        given(techStackRepository.findAllById(any()))
                .willReturn(List.of(techStack4, techStack5, techStack6));

        //when
        ProjectDto projectDto = projectService.updateProject(
                project.getId(),
                member.getId(),
                UpdateProjectForm.builder()
                        .title("수정 제목")
                        .techStackIds(newTech)
                        .build()
        );
        //then
        assertEquals(newTech.size(), projectDto.getProjectTechStacks().size());
        assertEquals(techStack3.getId(), projectDto.getProjectTechStacks().get(0).getTechStack().getId());
        assertEquals(techStack4.getId(), projectDto.getProjectTechStacks().get(1).getTechStack().getId());
        assertEquals(techStack5.getId(), projectDto.getProjectTechStacks().get(2).getTechStack().getId());
        assertEquals(techStack6.getId(), projectDto.getProjectTechStacks().get(3).getTechStack().getId());
        assertEquals("수정 제목", projectDto.getTitle());
        verify(projectTechStackRepository, times(1)).deleteAllByIdIn(List.of(1L, 2L));
        verify(projectTechStackRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("프로젝트 수정 실패 - 해당 프로젝트 없음")
    void updateProject_NotFoundProject() {
        //given

        given(projectRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.updateProject(1L, 9L, new UpdateProjectForm()));

        //then
        assertEquals(ErrorCode.NOT_FOUND_PROJECT, exception.getErrorCode());
    }

    @Test
    @DisplayName("프로젝트 수정 실패 - 해당 프로젝트 수정 권한 없음")
    void updateProject_NotMatchMemberProject() {

        //given
        given(projectRepository.findById(anyLong()))
                .willReturn(Optional.of(project));
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.updateProject(1L, 100L, new UpdateProjectForm()));

        //then
        assertEquals(ErrorCode.NOT_MATCH_MEMBER_PROJECT, exception.getErrorCode());
    }

    @Test
    @DisplayName("프로젝트 수정 실패 - 해당 기술스택 없음")
    void updateProject_NotFoundTechStack() {
        //given
        given(projectRepository.findById(anyLong()))
                .willReturn(Optional.of(project));
        given(techStackRepository.findAllById(any()))
                .willReturn(new ArrayList<>());

        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.updateProject(1L, 9L, new UpdateProjectForm(
                        "글 제목 수정",
                        "글 내용 수정",
                        1,
                        ProjectStatus.RECRUITING,
                        false,
                        "위치 수정",
                        LocalDate.of(2022, 9, 15),
                        List.of(1L, 2L)
                )));
        //then
        assertEquals(ErrorCode.NOT_FOUND_TECH_STACK, exception.getErrorCode());
    }

    @Test
    @DisplayName("프로젝트 상세조회 실패")
    void getProject_fail() {
        //given
        given(projectRepository.findByIdQuery(anyLong()))
                .willReturn(Optional.empty());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.getProject(1L));
        //then

        assertEquals(ErrorCode.NOT_FOUND_PROJECT, exception.getErrorCode());
    }

    @Test
    @DisplayName("프로젝트 상세조회 성공")
    void getProject_success() {
        member = Member.builder()
                .id(9L)
                .email("togather@to.com")
                .nickname("두개더")
                .profileImage("img_url")
                .build();


        project = Project.builder()
                .id(999L)
                .member(member)
                .title("제목999")
                .content("내용999")
                .personnel(10)
                .status(ProjectStatus.RECRUITING)
                .location("서울")
                .offline(true)
                .deadline(LocalDate.of(2022, 9, 12))
                .build();
        //given
        given(projectRepository.findByIdQuery(anyLong()))
                .willReturn(Optional.of(project));


        //when
        ProjectDetails projectDetails = projectService.getProject(1L);
        //then

        assertEquals(999L, projectDetails.getId());
    }

    @Test
    @DisplayName("프로젝트 삭제 실패 - 해당 프로젝트 없음")
    void deleteProject_NotFoundProject() {
        //given
        given(projectRepository.findByIdQuery(anyLong()))
                .willReturn(Optional.empty());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.deleteProject(1L, 9L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_PROJECT, exception.getErrorCode());
    }

    @Test
    @DisplayName("프로젝트 삭제 실패 - 해당 프로젝트 삭제 권한 없음")
    void name() {
        //given
        given(projectRepository.findByIdQuery(anyLong()))
                .willReturn(Optional.of(project));
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.deleteProject(1L, 1234L));
        //then
        assertEquals(ErrorCode.NOT_MATCH_MEMBER_PROJECT, exception.getErrorCode());
    }
}
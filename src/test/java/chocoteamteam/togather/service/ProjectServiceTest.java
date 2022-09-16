package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CreateProjectForm;
import chocoteamteam.togather.dto.ProjectDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
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
        assertEquals(techStacks.size(), projectDto.getProjectTechStacks().size());
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
                        "부산",
                        LocalDate.of(2022, 9, 13),
                        List.of(5L, 6L)
                )));
        //then
        assertEquals(ErrorCode.NOT_FOUND_TECH_STACK, exception.getErrorCode());
    }
}
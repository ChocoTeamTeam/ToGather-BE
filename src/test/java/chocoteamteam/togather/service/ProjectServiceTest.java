package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CreateProjectForm;
import chocoteamteam.togather.dto.ProjectDetails;
import chocoteamteam.togather.dto.ProjectDto;
import chocoteamteam.togather.dto.UpdateProjectForm;
import chocoteamteam.togather.entity.*;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.ProjectException;
import chocoteamteam.togather.repository.*;
import chocoteamteam.togather.type.ProjectStatus;
import chocoteamteam.togather.type.Role;
import chocoteamteam.togather.type.TechCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    @Mock
    private ProjectMemberRepository projectMemberRepository;
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
                .nickname("?????????")
                .profileImage("img_url")
                .build();


        project = Project.builder()
                .id(999L)
                .member(member)
                .title("??????999")
                .content("??????999")
                .personnel(10)
                .status(ProjectStatus.RECRUITING)
                .offline(false)
                .location(Location.builder()
                        .address("??????????????? ????????? ?????? ???????????? 231 ?????? ????????? 6??? 7???")
                        .latitude(37.503050)
                        .longitude(127.041583)
                        .build())
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
    @DisplayName("???????????? ?????? ??????")
    void createProjectSuccess() {
        //given
        ProjectMember projectMember = ProjectMember.builder()
                .id(1L)
                .member(member)
                .project(project)
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        given(projectRepository.save(any()))
                .willReturn(project);

        given(techStackRepository.findAllById(any()))
                .willReturn(techStacks);

        given(projectMemberRepository.save(any()))
                .willReturn(projectMember);

        //when
        CreateProjectForm form = new CreateProjectForm(
                "?????? ?????? ??????",
                "?????? ?????? ??????",
                1000,
                ProjectStatus.RECRUITING,
                false,
                "??????????????? ????????? ?????? ???????????? 231 ?????? ????????? 6??? 7???",
                37.503050,
                127.041583,
                LocalDate.of(2050, 9, 13),
                List.of(1000L, 1001L)
        );
        ProjectDto projectDto = projectService.createProject(8L, form);
        ArgumentCaptor<ProjectMember> captor = ArgumentCaptor.forClass(ProjectMember.class);

        //then
        assertEquals(project.getId(), projectDto.getId());
        assertEquals(member.getId(), projectDto.getMember().getId());
        assertEquals(project.getTitle(), projectDto.getTitle());
        assertEquals(project.getContent(), projectDto.getContent());
        assertEquals(project.getPersonnel(), projectDto.getPersonnel());
        assertEquals(project.getStatus(), projectDto.getStatus());
        assertEquals(project.getLocation(), projectDto.getLocation());
        assertEquals(project.getDeadline(), projectDto.getDeadline());
        assertEquals(techStacks.get(0).getName(), projectDto.getTechStacks().get(0).getName());
        assertEquals(project.getProjectTechStacks().size(), projectDto.getTechStacks().size());
        verify(projectTechStackRepository, times(1)).saveAll(any());
        verify(projectMemberRepository, times(1)).save(captor.capture());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ?????? ??????")
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
    @DisplayName("???????????? ?????? ?????? - ?????? ?????? ?????? ??????")
    void createProject_NotFoundTechStack() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        given(techStackRepository.findAllById(any()))
                .willReturn(new ArrayList<>());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.createProject(1L, new CreateProjectForm(
                        "??????888",
                        "??????888",
                        20,
                        ProjectStatus.RECRUITING,
                        false,
                        "??????????????? ????????? ?????? ???????????? 231 ?????? ????????? 6??? 7???",
                        37.503050,
                        127.041583,
                        LocalDate.of(2022, 9, 13),
                        List.of(5L, 6L)
                )));
        //then
        assertEquals(ErrorCode.NOT_FOUND_TECH_STACK, exception.getErrorCode());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    void updateProjectSuccess() {
        //given
        Project testProject = Project.builder()
                .id(project.getId())
                .member(member)
                .build();
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

        /*
         * ???????????? ?????? ?????? ?????? ????????? ?????? ?????? ?????? ???????????? ??????????????????
         * (ex ?????? ?????? ?????? 1??? - ?????? ?????? 1???)
         * ???????????? ???????????? ???????????????
         * ?????? ?????? ?????? ?????? ?????? ID??? ?????????,
         * ?????? ????????? ?????? ?????? ?????? ID??? ?????? ?????? ????????? ???????????? ?????? ?????? ?????? ????????? ????????? ???????????????
         * (???????????? ????????? ???????????? ?????? ??? ?????? ?????? ?????? ????????? ???????????????)
         * ?????? ?????? ?????? ID : 1,2,3
         * ?????? ?????? ?????? ?????? ID : 3,4,5,6
         * ????????? ??? ?????? ?????? ?????? ID : 1,2
         * ?????? ?????? ??? ?????? ?????? ID : 4,5,6
         * ?????? ?????? ?????? ?????? ID : 3,4,5,6
         */

        //?????? ???????????? ?????? ?????? ID
        List<Long> newTech = new ArrayList<>(List.of(3L, 4L, 5L, 6L));

        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.of(testProject));

        given(techStackRepository.findAllById(any()))
                .willReturn(List.of(techStack4, techStack5, techStack6));

        //when
        ProjectDto projectDto = projectService.updateProject(
                testProject.getId(),
                member.getId(),
                UpdateProjectForm.builder()
                        .title("?????? ??????")
                        .techStackIds(newTech)
                        .build()
        );
        //then
        assertEquals(newTech.size(), projectDto.getTechStacks().size());
        assertEquals(techStack3.getId(), projectDto.getTechStacks().get(0).getId());
        assertEquals(techStack4.getId(), projectDto.getTechStacks().get(1).getId());
        assertEquals(techStack5.getId(), projectDto.getTechStacks().get(2).getId());
        assertEquals(techStack6.getId(), projectDto.getTechStacks().get(3).getId());
        assertEquals("?????? ??????", projectDto.getTitle());
        verify(projectTechStackRepository, times(1)).deleteAllByIdInQuery(List.of(1L, 2L));
        verify(projectTechStackRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ???????????? ??????")
    void updateProject_NotFoundProject() {
        //given

        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.empty());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.updateProject(1L, 9L, new UpdateProjectForm()));

        //then
        assertEquals(ErrorCode.NOT_FOUND_PROJECT, exception.getErrorCode());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ???????????? ?????? ?????? ??????")
    void updateProject_NotMatchMemberProject() {

        //given
        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.of(project));
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.updateProject(1L, 100L, new UpdateProjectForm()));

        //then
        assertEquals(ErrorCode.NOT_MATCH_MEMBER_PROJECT, exception.getErrorCode());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ???????????? ??????")
    void updateProject_NotFoundTechStack() {
        //given
        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.of(project));
        given(techStackRepository.findAllById(any()))
                .willReturn(new ArrayList<>());

        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.updateProject(1L, 9L, new UpdateProjectForm(
                        "??? ?????? ??????",
                        "??? ?????? ??????",
                        1,
                        ProjectStatus.RECRUITING,
                        false,
                        "??????????????? ????????? ?????? ???????????? 231 ?????? ????????? 6??? 7???",
                        37.503050,
                        127.041583,
                        LocalDate.of(2022, 9, 15),
                        List.of(1L, 2L)
                )));
        //then
        assertEquals(ErrorCode.NOT_FOUND_TECH_STACK, exception.getErrorCode());
    }

    @Test
    @DisplayName("???????????? ???????????? ??????")
    void getProject_fail() {
        //given
        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.empty());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.getProject(1L));
        //then

        assertEquals(ErrorCode.NOT_FOUND_PROJECT, exception.getErrorCode());
    }

    @Test
    @DisplayName("???????????? ???????????? ?????? (?????? ??????)")
    void getProject_success() {

        project.addComment(Comment.builder().member(member).id(1L).build());
        project.addComment(Comment.builder().member(member).id(2L).build());
        project.addComment(Comment.builder().member(member).id(3L).build());
        //given
        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.of(project));

        //when
        ProjectDetails projectDetails = projectService.getProject(1L);
        //then

        assertEquals(999L, projectDetails.getId());
        assertEquals(3, projectDetails.getComments().size());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ???")
    void deleteProject_MyProject() {
        //given
        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.of(project));
        //when
        projectService.deleteProject(1L, member.getId(), Role.ROLE_USER);
        //then
        verify(projectRepository, times(1)).deleteById(project.getId());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ADMIN")
    void deleteProject_byAdmin() {
        //given
        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.of(project));
        //when
        projectService.deleteProject(3L, 1234L, Role.ROLE_ADMIN);
        //then
        verify(projectRepository, times(1)).deleteById(project.getId());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ???????????? ??????")
    void deleteProject_NotFoundProject() {
        //given
        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.empty());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.deleteProject(1L, 1234L, Role.ROLE_USER));

        //then
        assertEquals(ErrorCode.NOT_FOUND_PROJECT, exception.getErrorCode());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ???????????? ?????? ?????? ?????? (?????? ??? x)")
    void deleteProject_NotMyProject() {
        //given
        given(projectRepository.findByIdWithMemberAndTechStack(anyLong()))
                .willReturn(Optional.of(project));
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> projectService.deleteProject(1L, 1234L, Role.ROLE_USER));
        //then
        assertEquals(ErrorCode.NOT_MATCH_MEMBER_PROJECT, exception.getErrorCode());
    }
}
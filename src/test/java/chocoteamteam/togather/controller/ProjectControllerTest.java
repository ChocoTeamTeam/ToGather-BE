package chocoteamteam.togather.controller;

import chocoteamteam.togather.config.SecurityConfig;
import chocoteamteam.togather.dto.*;
import chocoteamteam.togather.service.JwtService;
import chocoteamteam.togather.service.ProjectApplicantService;
import chocoteamteam.togather.service.ProjectService;
import chocoteamteam.togather.testUtils.WithLoginMember;
import chocoteamteam.togather.type.ProjectStatus;
import chocoteamteam.togather.type.TechCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(value = ProjectController.class,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class ProjectControllerTest {
    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectApplicantService projectApplicantService;

    @MockBean
    JwtService jwtService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    MemberDto memberDto;
    ProjectDto projectDto;
    TechStackDto techStackDto1, techStackDto2;
    List<TechStackDto> techStackDtos = new ArrayList<>();

    ProjectDetails projectDetails;
    CommentDto commentDto1, commentDto2;
    List<CommentDto> commentDtos = new ArrayList<>();

    @BeforeAll
    public void beforeAll() {
        techStackDto1 = TechStackDto.builder()
                .id(1L)
                .name("techStack1")
                .category(TechCategory.BACKEND)
                .image("img1")
                .build();
        techStackDto2 = TechStackDto.builder()
                .id(2L)
                .name("techStack2")
                .category(TechCategory.BACKEND)
                .image("img2")
                .build();
        techStackDtos.add(techStackDto1);
        techStackDtos.add(techStackDto2);

        memberDto = MemberDto.builder()
                .id(1L)
                .email("email1@naver.com")
                .nickname("nick1")
                .profileImage("img1")
                .build();

        projectDto = ProjectDto.builder()
                .id(1L)
                .member(memberDto)
                .title("글 제목")
                .content("글 내용")
                .personnel(5)
                .status(ProjectStatus.RECRUITING)
                .offline(true)
                .location("서울")
                .deadline(LocalDate.of(2022, 10, 5))
                .techStacks(techStackDtos)
                .build();

        commentDto1 = CommentDto.builder()
                .id(1L)
                .member(memberDto)
                .content("댓글 내용1")
                .createdAt(LocalDateTime.now())
                .build();

        commentDto2 = CommentDto.builder()
                .id(2L)
                .member(memberDto)
                .content("댓글 내용2")
                .createdAt(LocalDateTime.now())
                .build();

        commentDtos.add(commentDto1);
        commentDtos.add(commentDto2);

        projectDetails = ProjectDetails.builder()
                .id(1L)
                .member(memberDto)
                .title("글 제목")
                .content("글 내용")
                .personnel(5)
                .status(ProjectStatus.RECRUITING)
                .offline(true)
                .location("서울")
                .deadline(LocalDate.of(2022, 10, 5))
                .techStacks(techStackDtos)
                .comments(commentDtos)
                .build();
    }

    @WithLoginMember
    @Test
    @DisplayName("프로젝트 등록 성공")
    void createProjectSuccess() throws Exception {
        //given
        given(projectService.createProject(anyLong(), any()))
                .willReturn(projectDto);
        //when
        //then
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CreateProjectForm.builder()
                                .title("아무 제목")
                                .content("아무 내용")
                                .personnel(100)
                                .status(ProjectStatus.RECRUITING)
                                .offline(true)
                                .location("아무 장소")
                                .deadline(LocalDate.of(2050, 1, 1))
                                .techStackIds(List.of(1L, 2L))
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectDto.getId()))
                .andExpect(jsonPath("$.member.id").value(projectDto.getMember().getId()))
                .andExpect(jsonPath("$.member.email").value(projectDto.getMember().getEmail()))
                .andExpect(jsonPath("$.member.nickname").value(projectDto.getMember().getNickname()))
                .andExpect(jsonPath("$.member.profileImage").value(projectDto.getMember().getProfileImage()))
                .andExpect(jsonPath("$.title").value(projectDto.getTitle()))
                .andExpect(jsonPath("$.content").value(projectDto.getContent()))
                .andExpect(jsonPath("$.personnel").value(projectDto.getPersonnel()))
                .andExpect(jsonPath("$.status").value(projectDto.getStatus().name()))
                .andExpect(jsonPath("$.offline").value(projectDto.getOffline()))
                .andExpect(jsonPath("$.location").value(projectDto.getLocation()))
                .andExpect(jsonPath("$.deadline").value(projectDto.getDeadline().toString()))
                .andExpect(jsonPath("$.techStacks", hasSize(2)))
                .andExpect(jsonPath("$.techStacks[0].id").value(techStackDtos.get(0).getId()))
                .andExpect(jsonPath("$.techStacks[0].name").value(techStackDtos.get(0).getName()))
                .andExpect(jsonPath("$.techStacks[0].category").value(techStackDtos.get(0).getCategory().name()))
                .andExpect(jsonPath("$.techStacks[0].image").value(techStackDtos.get(0).getImage()))
                .andExpect(jsonPath("$.techStacks[1].id").value(techStackDtos.get(1).getId()))
                .andExpect(jsonPath("$.techStacks[1].name").value(techStackDtos.get(1).getName()))
                .andExpect(jsonPath("$.techStacks[1].category").value(techStackDtos.get(1).getCategory().name()))
                .andExpect(jsonPath("$.techStacks[1].image").value(techStackDtos.get(1).getImage()))
                .andDo(print());
    }

    @WithLoginMember
    @Test
    @DisplayName("프로젝트 수정 성공")
    void updateProjectSuccess() throws Exception {
        //given
        given(projectService.updateProject(anyLong(), anyLong(), any()))
                .willReturn(projectDto);
        //when
        //then
        mockMvc.perform(put("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UpdateProjectForm.builder()
                                .title("아무 제목")
                                .content("아무 내용")
                                .personnel(100)
                                .status(ProjectStatus.RECRUITING)
                                .offline(true)
                                .location("아무 장소")
                                .deadline(LocalDate.of(2050, 1, 1))
                                .techStackIds(List.of(1L, 2L))
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectDto.getId()))
                .andExpect(jsonPath("$.member.id").value(projectDto.getMember().getId()))
                .andExpect(jsonPath("$.member.email").value(projectDto.getMember().getEmail()))
                .andExpect(jsonPath("$.member.nickname").value(projectDto.getMember().getNickname()))
                .andExpect(jsonPath("$.member.profileImage").value(projectDto.getMember().getProfileImage()))
                .andExpect(jsonPath("$.title").value(projectDto.getTitle()))
                .andExpect(jsonPath("$.content").value(projectDto.getContent()))
                .andExpect(jsonPath("$.personnel").value(projectDto.getPersonnel()))
                .andExpect(jsonPath("$.status").value(projectDto.getStatus().name()))
                .andExpect(jsonPath("$.offline").value(projectDto.getOffline()))
                .andExpect(jsonPath("$.location").value(projectDto.getLocation()))
                .andExpect(jsonPath("$.deadline").value(projectDto.getDeadline().toString()))
                .andExpect(jsonPath("$.techStacks", hasSize(2)))
                .andExpect(jsonPath("$.techStacks[0].id").value(techStackDtos.get(0).getId()))
                .andExpect(jsonPath("$.techStacks[0].name").value(techStackDtos.get(0).getName()))
                .andExpect(jsonPath("$.techStacks[0].category").value(techStackDtos.get(0).getCategory().name()))
                .andExpect(jsonPath("$.techStacks[0].image").value(techStackDtos.get(0).getImage()))
                .andExpect(jsonPath("$.techStacks[1].id").value(techStackDtos.get(1).getId()))
                .andExpect(jsonPath("$.techStacks[1].name").value(techStackDtos.get(1).getName()))
                .andExpect(jsonPath("$.techStacks[1].category").value(techStackDtos.get(1).getCategory().name()))
                .andExpect(jsonPath("$.techStacks[1].image").value(techStackDtos.get(1).getImage()))
                .andDo(print());
    }

    @Test
    @DisplayName("프로젝트 상세 조회 성공")
    void getProjectSuccess() throws Exception {
        //given
        given(projectService.getProject(anyLong()))
                .willReturn(projectDetails);
        //when
        //then
        mockMvc.perform(get("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UpdateProjectForm.builder()
                                .title("아무 제목")
                                .content("아무 내용")
                                .personnel(100)
                                .status(ProjectStatus.RECRUITING)
                                .offline(true)
                                .location("아무 장소")
                                .deadline(LocalDate.of(2050, 1, 1))
                                .techStackIds(List.of(1L, 2L))
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectDto.getId()))
                .andExpect(jsonPath("$.member.id").value(projectDto.getMember().getId()))
                .andExpect(jsonPath("$.member.email").value(projectDto.getMember().getEmail()))
                .andExpect(jsonPath("$.member.nickname").value(projectDto.getMember().getNickname()))
                .andExpect(jsonPath("$.member.profileImage").value(projectDto.getMember().getProfileImage()))
                .andExpect(jsonPath("$.title").value(projectDto.getTitle()))
                .andExpect(jsonPath("$.content").value(projectDto.getContent()))
                .andExpect(jsonPath("$.personnel").value(projectDto.getPersonnel()))
                .andExpect(jsonPath("$.status").value(projectDto.getStatus().name()))
                .andExpect(jsonPath("$.offline").value(projectDto.getOffline()))
                .andExpect(jsonPath("$.location").value(projectDto.getLocation()))
                .andExpect(jsonPath("$.deadline").value(projectDto.getDeadline().toString()))
                .andExpect(jsonPath("$.techStacks", hasSize(2)))
                .andExpect(jsonPath("$.techStacks[0].id").value(techStackDtos.get(0).getId()))
                .andExpect(jsonPath("$.techStacks[0].name").value(techStackDtos.get(0).getName()))
                .andExpect(jsonPath("$.techStacks[0].category").value(techStackDtos.get(0).getCategory().name()))
                .andExpect(jsonPath("$.techStacks[0].image").value(techStackDtos.get(0).getImage()))
                .andExpect(jsonPath("$.techStacks[1].id").value(techStackDtos.get(1).getId()))
                .andExpect(jsonPath("$.techStacks[1].name").value(techStackDtos.get(1).getName()))
                .andExpect(jsonPath("$.techStacks[1].category").value(techStackDtos.get(1).getCategory().name()))
                .andExpect(jsonPath("$.techStacks[1].image").value(techStackDtos.get(1).getImage()))
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.comments[0].id").value(commentDtos.get(0).getId()))
                .andExpect(jsonPath("$.comments[0].member.id").value(commentDtos.get(0).getMember().getId()))
                .andExpect(jsonPath("$.comments[0].content").value(commentDtos.get(0).getContent()))
                .andExpect(jsonPath("$.comments[0].createdAt").value(commentDtos.get(0).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .andExpect(jsonPath("$.comments[1].id").value(commentDtos.get(1).getId()))
                .andExpect(jsonPath("$.comments[1].member.id").value(commentDtos.get(1).getMember().getId()))
                .andExpect(jsonPath("$.comments[1].content").value(commentDtos.get(1).getContent()))
                .andExpect(jsonPath("$.comments[1].createdAt").value(commentDtos.get(1).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .andDo(print());
    }

    @WithLoginMember
    @Test
    @DisplayName("프로젝트 삭제 성공")
    void deleteProjectSuccess() throws Exception {
        //given
        given(projectService.deleteProject(anyLong(), anyLong(), any()))
                .willReturn(projectDto);
        //when
        //then
        mockMvc.perform(delete("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UpdateProjectForm.builder()
                                .title("아무 제목")
                                .content("아무 내용")
                                .personnel(100)
                                .status(ProjectStatus.RECRUITING)
                                .offline(true)
                                .location("아무 장소")
                                .deadline(LocalDate.of(2050, 1, 1))
                                .techStackIds(List.of(1L, 2L))
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectDto.getId()))
                .andExpect(jsonPath("$.member.id").value(projectDto.getMember().getId()))
                .andExpect(jsonPath("$.member.email").value(projectDto.getMember().getEmail()))
                .andExpect(jsonPath("$.member.nickname").value(projectDto.getMember().getNickname()))
                .andExpect(jsonPath("$.member.profileImage").value(projectDto.getMember().getProfileImage()))
                .andExpect(jsonPath("$.title").value(projectDto.getTitle()))
                .andExpect(jsonPath("$.content").value(projectDto.getContent()))
                .andExpect(jsonPath("$.personnel").value(projectDto.getPersonnel()))
                .andExpect(jsonPath("$.status").value(projectDto.getStatus().name()))
                .andExpect(jsonPath("$.offline").value(projectDto.getOffline()))
                .andExpect(jsonPath("$.location").value(projectDto.getLocation()))
                .andExpect(jsonPath("$.deadline").value(projectDto.getDeadline().toString()))
                .andExpect(jsonPath("$.techStacks", hasSize(2)))
                .andExpect(jsonPath("$.techStacks[0].id").value(techStackDtos.get(0).getId()))
                .andExpect(jsonPath("$.techStacks[0].name").value(techStackDtos.get(0).getName()))
                .andExpect(jsonPath("$.techStacks[0].category").value(techStackDtos.get(0).getCategory().name()))
                .andExpect(jsonPath("$.techStacks[0].image").value(techStackDtos.get(0).getImage()))
                .andExpect(jsonPath("$.techStacks[1].id").value(techStackDtos.get(1).getId()))
                .andExpect(jsonPath("$.techStacks[1].name").value(techStackDtos.get(1).getName()))
                .andExpect(jsonPath("$.techStacks[1].category").value(techStackDtos.get(1).getCategory().name()))
                .andExpect(jsonPath("$.techStacks[1].image").value(techStackDtos.get(1).getImage()))
                .andDo(print());
    }
}
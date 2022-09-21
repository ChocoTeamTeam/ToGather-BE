package chocoteamteam.togather.dto;

import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.type.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetails {
    private Long id;
    private MemberDto member;
    private String title;
    private String content;
    private Integer personnel;
    private ProjectStatus status;
    private String location;
    private LocalDate deadline;
    private boolean offline;
    private List<TechStackDto> techStacks;
    private List<CommentDto> comments;

    public static ProjectDetails fromEntity(Project project) {
        return ProjectDetails.builder()
                .id(project.getId())
                .member(MemberDto.from(project.getMember()))
                .title(project.getTitle())
                .content(project.getContent())
                .personnel(project.getPersonnel())
                .status(project.getStatus())
                .location(project.getLocation())
                .deadline(project.getDeadline())
                .offline(project.getOffline())
                .techStacks(project.getProjectTechStacks()
                        .stream()
                        .map(projectTechStack -> TechStackDto.from(projectTechStack.getTechStack()))
                        .collect(Collectors.toList()))
                .comments(project.getComments().stream()
                        .map(CommentDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
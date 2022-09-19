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
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private MemberDto member;
    private String title;
    private String content;
    private Integer personnel;
    private ProjectStatus status;
    private Boolean offline;
    private String location;
    private LocalDate deadline;
    private List<ProjectTechStackDto> projectTechStacks;

    public static ProjectDto from(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .member(MemberDto.from(project.getMember()))
                .title(project.getTitle())
                .content(project.getContent())
                .personnel(project.getPersonnel())
                .status(project.getStatus())
                .offline(project.getOffline())
                .location(project.getLocation())
                .deadline(project.getDeadline())
                .projectTechStacks(project.getProjectTechStacks().stream()
                        .map(ProjectTechStackDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
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
public class SimpleProject {
    private Long id;
    private MemberDto member;
    private String title;
    private Integer personnel;
    private ProjectStatus status;
    private String location;
    private LocalDate deadline;
    private List<TechStackDto> techStacks;

    /*  목록 조회용 simple 버전
    * */
    public static SimpleProject from(Project project) {
        return SimpleProject.builder()
                .id(project.getId())
                .member(MemberDto.from(project.getMember()))
                .title(project.getTitle())
                .personnel(project.getPersonnel())
                .status(project.getStatus())
                .location(project.getLocation())
                .deadline(project.getDeadline())
                .techStacks(project.getProjectTechStacks()
                        .stream()
                        .map(projectTechStack -> TechStackDto.from(projectTechStack.getTechStack()))
                        .collect(Collectors.toList()))
                .build();
    }
}

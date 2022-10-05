package chocoteamteam.togather.dto;

import chocoteamteam.togather.entity.Location;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.type.ProjectStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMapResponse {
    private Long id;
    private String title;
    private Integer personnel;
    private ProjectStatus status;
    private Location location;

    public static ProjectMapResponse fromEntity(Project project) {
        return ProjectMapResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .personnel(project.getPersonnel())
                .status(project.getStatus())
                .location(project.getLocation())
                .build();
    }
}
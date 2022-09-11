package chocoteamteam.togather.dto;

import chocoteamteam.togather.entity.ProjectTech;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTechDto {
    Long id;
    private Long projectId;
    private TechStackDto techStack;

    public static ProjectTechDto from(ProjectTech projectTech) {
        return ProjectTechDto.builder()
                .id(projectTech.getId())
                .projectId(projectTech.getProject().getId())
                .techStack(TechStackDto.from(projectTech.getTechStack()))
                .build();
    }

}

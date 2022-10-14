package chocoteamteam.togather.dto;

import chocoteamteam.togather.entity.ProjectTechStack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTechStackDto {
    private Long id;
    private Long projectId;
    private TechStackDto techStack;

    public static ProjectTechStackDto from(ProjectTechStack projectTechStack) {
        return ProjectTechStackDto.builder()
                .id(projectTechStack.getId())
                .projectId(projectTechStack.getProject().getId())
                .techStack(TechStackDto.from(projectTechStack.getTechStack()))
                .build();
    }
}
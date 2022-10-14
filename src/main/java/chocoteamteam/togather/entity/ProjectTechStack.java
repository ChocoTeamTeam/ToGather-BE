package chocoteamteam.togather.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "project_tech_stack_check",
                columnNames = {"project_id", "tech_stack_id"})
})
public class ProjectTechStack extends BaseTimeEntity {

    public ProjectTechStack(Project project, TechStack techStack) {
        setProject(project);
        this.techStack = techStack;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TechStack techStack;

    public void setProject(Project project) {
        if (this.project != null) {
            this.project.getProjectTechStacks().remove(this);
        }
        this.project = project;
        project.getProjectTechStacks().add(this);
    }
}
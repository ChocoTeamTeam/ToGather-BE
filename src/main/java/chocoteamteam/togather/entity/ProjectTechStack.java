package chocoteamteam.togather.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "project_tech_stack_check",
                columnNames = {"project_id", "tech_stack_id"})
})
public class ProjectTechStack extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tech_stack_id")
    private TechStack techStack;

    public void setProject(Project project) {
        if (this.project != null) {
            this.project.getProjectTechStacks().remove(this);
        }
        this.project = project;
        project.getProjectTechStacks().add(this);
    }
}
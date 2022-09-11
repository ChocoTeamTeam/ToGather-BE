package chocoteamteam.togather.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "project_techs")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProjectTech extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "tech_stack_id")
    private TechStack techStack;

    public void setProject(Project project) {
        if (this.project != null) {
            this.project.getProjectTechs().remove(this);
        }
        this.project = project;
        if (!project.getProjectTechs().contains(this)) {
            project.addProjectTech(this);
        }
    }
}

package chocoteamteam.togather.entity;

import chocoteamteam.togather.dto.UpdateProjectForm;
import chocoteamteam.togather.type.ProjectStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    private String title;

    @Lob
    private String content;

    private Integer personnel;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String location;

    private LocalDate deadline;

    @OneToMany(mappedBy = "project")
    private final List<ProjectTechStack> projectTechStacks = new ArrayList<>();

    public void update(UpdateProjectForm form) {
        this.title = form.getTitle();
        this.content = form.getContent();
        this.personnel = form.getPersonnel();
        this.status = form.getStatus();
        this.location = form.getLocation();
        this.deadline = form.getDeadline();
    }
}
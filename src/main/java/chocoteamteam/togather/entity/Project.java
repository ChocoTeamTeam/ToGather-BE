package chocoteamteam.togather.entity;

import chocoteamteam.togather.dto.CreateProjectForm;
import chocoteamteam.togather.dto.UpdateProjectForm;
import chocoteamteam.togather.type.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO Team, Member 연관관계 정리 필요
    @ManyToOne
    @JoinColumn(name = "member_id")
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
    private List<ProjectTech> projectTechs = new ArrayList<>();

    public void addProjectTech(ProjectTech projectTech) {
        this.projectTechs.add(projectTech);
        if (projectTech.getProject() != this) {
            projectTech.setProject(this);
        }
    }

    public static Project of(Member member, CreateProjectForm form) {
        return Project.builder()
                .member(member)
                .title(form.getTitle())
                .content(form.getContent())
                .personnel(form.getPersonnel())
                .status(ProjectStatus.RECRUITING)
                .location(form.getLocation())
                .projectTechs(new ArrayList<>())
                .deadline(form.getDeadline())
                .build();
    }

    public void update(
            UpdateProjectForm form,
            List<ProjectTech> projectTechs
    ) {
        this.title = form.getTitle();
        this.content = form.getContent();
        this.personnel = form.getPersonnel();
        this.status = form.getStatus();
        this.location = form.getLocation();
        this.deadline = form.getDeadline();
        this.projectTechs = projectTechs;
    }

}
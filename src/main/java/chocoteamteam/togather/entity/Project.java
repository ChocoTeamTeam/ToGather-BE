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
@Table(indexes = {@Index(name = "status_index", columnList = "status")})
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

    @Enumerated(EnumType.STRING) // 인덱싱하기
    private ProjectStatus status;

    private Boolean offline;
    private String location;

    private LocalDate deadline;

    @OneToMany(mappedBy = "project")
    private final List<ProjectTechStack> projectTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setProject(this);
    }

    public void update(UpdateProjectForm form) {
        this.title = form.getTitle();
        this.content = form.getContent();
        this.personnel = form.getPersonnel();
        this.status = form.getStatus();
        this.offline = form.getOffline();
        this.location = form.getLocation();
        this.deadline = form.getDeadline();
    }
}
package chocoteamteam.togather.entity;

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
    private final List<ProjectTechStack> projectTechStacks = new ArrayList<>();
}
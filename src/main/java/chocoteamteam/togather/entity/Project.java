package chocoteamteam.togather.entity;

import chocoteamteam.togather.type.ProjectStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member writer;

    private String title;

    private String content;

    private Integer personnel;

    private ProjectStatus status;

    private String location;

}
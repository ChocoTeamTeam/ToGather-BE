package chocoteamteam.togather.entity;

import chocoteamteam.togather.type.TechCategory;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TechStack extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private TechCategory category;
    private String image;
    @OneToMany(mappedBy = "techStack")
    private List<MemberTechStack> memberTechStacks = new ArrayList<>();

}
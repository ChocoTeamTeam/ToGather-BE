package chocoteamteam.togather.entity;

import chocoteamteam.togather.dto.UpdateTechStackForm;
import chocoteamteam.togather.type.TechCategory;
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

    public void update(UpdateTechStackForm form) {
        this.name = form.getName();
        this.category = form.getCategory();
        this.image = form.getImage();
    }
}
package chocoteamteam.togather.batch.domain.entity;

import chocoteamteam.togather.type.TechCategory;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WeeklyTechStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long techStackId;
    private String techStackName;

    @Enumerated(EnumType.STRING)
    private TechCategory techStackCategory;

    private Long count;
    private Integer weeks;
}

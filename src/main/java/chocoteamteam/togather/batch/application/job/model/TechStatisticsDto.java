package chocoteamteam.togather.batch.application.job.model;

import chocoteamteam.togather.type.TechCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TechStatisticsDto {
    private Long techStackId;
    private String techStackName;
    private TechCategory techStackCategory;
    private Long count;
}

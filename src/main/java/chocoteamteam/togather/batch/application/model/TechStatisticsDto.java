package chocoteamteam.togather.batch.application.model;

import chocoteamteam.togather.type.TechCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TechStatisticsDto {
    private Long techStackId;
    private String techStackName;
    private TechCategory techStackCategory;
    private Long count;
}

package chocoteamteam.togather.dto;

import chocoteamteam.togather.batch.application.job.model.TechStatisticsDto;
import chocoteamteam.togather.batch.domain.entity.WeeklyTechStatistics;
import chocoteamteam.togather.type.TechCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WeeklyTechStackStatisticsResponse {
    private Integer weeks;

    private final List<TechStatisticsDto> techStatistics = new ArrayList<>();

    public WeeklyTechStackStatisticsResponse fromEntity(List<WeeklyTechStatistics> weeklyTechStatistics, Integer weeks) {
        this.weeks = weeks;

        for (WeeklyTechStatistics weeklyTechStatistic : weeklyTechStatistics) {
            techStatistics.add(TechStatisticsDto.builder()
                    .techStackId(weeklyTechStatistic.getTechStackId())
                    .techStackName(weeklyTechStatistic.getTechStackName())
                    .techStackCategory(weeklyTechStatistic.getTechStackCategory())
                    .count(weeklyTechStatistic.getCount())
                    .build());
        }

        return this;
    }
}

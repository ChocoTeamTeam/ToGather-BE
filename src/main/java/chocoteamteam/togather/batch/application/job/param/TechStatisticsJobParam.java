package chocoteamteam.togather.batch.application.job.param;

import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@JobScope
@Component
public class TechStatisticsJobParam {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer weeks;

    @Value("#{jobParameters[startDate]}")
    private void setStartDate(String startDate) {
        this.startDate = LocalDateTime.parse(startDate);
    }

    @Value("#{jobParameters[endDate]}")
    private void setEndDate(String endDate) {
        this.endDate = LocalDateTime.parse(endDate);
    }

    @Value("#{jobParameters[weeks]}")
    private void setWeeks(Integer weeks) {
        this.weeks = weeks;
    }

}

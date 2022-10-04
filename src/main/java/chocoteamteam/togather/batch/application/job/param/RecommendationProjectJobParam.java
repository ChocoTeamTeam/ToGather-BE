package chocoteamteam.togather.batch.application.job.param;

import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@JobScope
@Component
public class RecommendationProjectJobParam {

    private LocalDate startDate;
    private LocalDate endDate;

    @Value("#{jobParameters[startDate]}")
    private void setStartDate(String startDate) {
        this.startDate = LocalDate.from(LocalDate.parse(startDate));
    }

    @Value("#{jobParameters[endDate]}")
    private void setEndDate(String endDate) {
        this.endDate = LocalDate.from(LocalDate.parse(endDate));
    }

}

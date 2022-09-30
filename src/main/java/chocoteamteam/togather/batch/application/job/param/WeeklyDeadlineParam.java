package chocoteamteam.togather.batch.application.job.param;

import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@JobScope
@Getter
public class WeeklyDeadlineParam {
    private final int PLUS_DAYS = 6;
    private LocalDate fromDate;
    private LocalDate toDate;

    @Value("#{jobParameters[fromDate]}")
    private void setDate(String fromDate) {
        this.fromDate = LocalDate.from(LocalDate.parse(fromDate));
        this.toDate = this.fromDate.plusDays(PLUS_DAYS);
    }
}
package chocoteamteam.togather.batch.application.job.param;

import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@JobScope
@Component
public class ProjectStatusJobParam {
    private LocalDate nowDate;

    @Value("#{jobParameters[nowDate]}")
    private void setStartDay(String nowDate) {
        this.nowDate = LocalDate.parse(nowDate);
    }
}
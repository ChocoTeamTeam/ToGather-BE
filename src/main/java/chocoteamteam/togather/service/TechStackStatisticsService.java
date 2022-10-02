package chocoteamteam.togather.service;

import chocoteamteam.togather.batch.domain.repository.WeeklyTechStatisticsRepository;
import chocoteamteam.togather.dto.WeeklyTechStackStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

@RequiredArgsConstructor
@Service
public class TechStackStatisticsService {
    private final WeeklyTechStatisticsRepository weeklyTechStatisticsRepository;

    public WeeklyTechStackStatisticsResponse getWeeklyStatistics() {
        int weeks = LocalDate.now().minusDays(7).get(WeekFields.ISO.weekOfYear());
        return new WeeklyTechStackStatisticsResponse().fromEntity(weeklyTechStatisticsRepository.findAllByWeeks(weeks), weeks);
    }

    public WeeklyTechStackStatisticsResponse getWeeklyStatistics(int weeks) {
        return new WeeklyTechStackStatisticsResponse().fromEntity(weeklyTechStatisticsRepository.findAllByWeeks(weeks), weeks);
    }
}

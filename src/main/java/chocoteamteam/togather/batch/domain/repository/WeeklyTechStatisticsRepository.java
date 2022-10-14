package chocoteamteam.togather.batch.domain.repository;

import chocoteamteam.togather.batch.domain.entity.WeeklyTechStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyTechStatisticsRepository extends JpaRepository<WeeklyTechStatistics, Long> {
    List<WeeklyTechStatistics> findAllByWeeks(Integer weeks);
}

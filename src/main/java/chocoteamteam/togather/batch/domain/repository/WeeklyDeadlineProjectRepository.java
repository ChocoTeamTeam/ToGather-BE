package chocoteamteam.togather.batch.domain.repository;

import chocoteamteam.togather.batch.domain.entity.WeeklyDeadlineProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyDeadlineProjectRepository extends JpaRepository<WeeklyDeadlineProject, Long> {
}
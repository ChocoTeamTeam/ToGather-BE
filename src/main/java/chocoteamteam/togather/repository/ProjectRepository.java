package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.type.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, QueryDslProjectRepository {

    Page<Project> findAllByStatusAndDeadlineBefore(ProjectStatus status, LocalDate deadline, Pageable pageable);
}
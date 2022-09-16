package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
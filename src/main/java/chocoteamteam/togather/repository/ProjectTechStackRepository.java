package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.ProjectTechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {
    void deleteAllByProjectId(Long projectId);
}
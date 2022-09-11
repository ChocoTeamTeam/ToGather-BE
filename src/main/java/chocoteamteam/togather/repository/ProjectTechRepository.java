package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.ProjectTech;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTechRepository extends JpaRepository<ProjectTech,Long> {
    void deleteAllByProjectId(Long projectId);
}

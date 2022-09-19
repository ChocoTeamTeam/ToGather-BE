package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.ProjectTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {

    @Query(value = "select tech_stack_id from project_tech_stack where project_id=:id", nativeQuery = true)
    List<Long> findTechStackIdsByProjectId(@Param("id") Long projectId);

    @Modifying
    List<ProjectTechStack> deleteAllByIdIn(List<Long> ids);
}
package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.ProjectTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {

    @Transactional
    @Modifying
    @Query("delete from ProjectTechStack pt where pt.id in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);

    void deleteByProjectId(Long projectId);
}
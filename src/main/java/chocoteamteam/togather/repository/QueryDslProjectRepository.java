package chocoteamteam.togather.repository;

import chocoteamteam.togather.dto.ProjectCondition;
import chocoteamteam.togather.entity.Project;

import java.util.List;
import java.util.Optional;

public interface QueryDslProjectRepository {
    List<Project> findAllOptionAndSearch(ProjectCondition projectCondition);

    Optional<Project> findByIdQuery(Long projectId);

}
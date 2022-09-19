package chocoteamteam.togather.repository;

import chocoteamteam.togather.dto.ProjectCondition;
import chocoteamteam.togather.dto.queryDslSimpleDto.SimpleProjectDto;
import chocoteamteam.togather.entity.Project;

import java.util.List;
import java.util.Optional;

public interface QueryDslProjectRepository {
    List<SimpleProjectDto> findAllOptionAndSearch(ProjectCondition projectCondition);

    Optional<Project> findByIdQuery(Long projectId);
}
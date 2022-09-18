package chocoteamteam.togather.repository;

import chocoteamteam.togather.dto.ProjectCondition;
import chocoteamteam.togather.dto.queryDslSimpleDto.SimpleProjectDto;

import java.util.List;

public interface QueryDslProjectRepository {
    List<SimpleProjectDto> findAllOptionAndSearch(ProjectCondition projectCondition);

}
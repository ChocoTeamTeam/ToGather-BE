package chocoteamteam.togather.repository;

import chocoteamteam.togather.dto.InterestDetail;
import chocoteamteam.togather.dto.ProjectCondition;
import chocoteamteam.togather.dto.queryDslSimpleDto.SimpleProjectDto;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.entity.ProjectMember;

import java.util.List;
import java.util.Optional;

public interface QueryDslProjectRepository {
    List<SimpleProjectDto> findAllOptionAndSearch(ProjectCondition projectCondition);

    List<InterestDetail> findAllInterestProjectByIds(List<Long> projectIds);

    Optional<Project> findByIdWithMemberAndTechStack(Long projectId);

    List<SimpleProjectDto> findAllByMemberId(Long memberId);

    List<ProjectMember> findAllByProjectMemberId(Long memberId);
}
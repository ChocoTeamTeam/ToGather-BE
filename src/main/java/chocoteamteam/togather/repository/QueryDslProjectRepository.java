package chocoteamteam.togather.repository;

import chocoteamteam.togather.batch.application.model.MemberRecommendationProjectDto;
import chocoteamteam.togather.dto.InterestDetail;
import chocoteamteam.togather.dto.ProjectCondition;
import chocoteamteam.togather.dto.queryDslSimpleDto.SimpleProjectDto;
import chocoteamteam.togather.entity.Project;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QueryDslProjectRepository {
    List<SimpleProjectDto> findAllOptionAndSearch(ProjectCondition projectCondition);

    List<InterestDetail> findAllInterestProjectByIds(List<Long> projectIds);

    Optional<Project> findByIdWithMemberAndTechStack(Long projectId);

    List<MemberRecommendationProjectDto> findAllByTechStackIdsAndDeadline(
            @Param("techStackIds") List<Long> techStackIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
package chocoteamteam.togather.repository.impl;


import chocoteamteam.togather.batch.application.model.MemberRecommendationProjectDto;
import chocoteamteam.togather.dto.InterestDetail;
import chocoteamteam.togather.dto.ProjectCondition;
import chocoteamteam.togather.dto.queryDslSimpleDto.QSimpleMemberDto;
import chocoteamteam.togather.dto.queryDslSimpleDto.QSimpleProjectDto;
import chocoteamteam.togather.dto.queryDslSimpleDto.QSimpleTechStackDto;
import chocoteamteam.togather.dto.queryDslSimpleDto.SimpleProjectDto;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.repository.QueryDslProjectRepository;
import chocoteamteam.togather.type.ProjectStatus;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static chocoteamteam.togather.entity.QMember.member;
import static chocoteamteam.togather.entity.QProject.project;
import static chocoteamteam.togather.entity.QProjectTechStack.projectTechStack;
import static chocoteamteam.togather.entity.QTechStack.techStack;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
@Repository
public class QueryDslProjectRepositoryImpl implements QueryDslProjectRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<InterestDetail> findAllInterestProjectByIds(List<Long> projectIds) {
        return new ArrayList<>(jpaQueryFactory
                .select(Projections.fields(InterestDetail.class,
                        project.id.as("projectId"),
                        project.title.as("title"),
                        project.status.as("status"),
                        project.deadline.as("deadline"),
                        project.member.nickname.as("writer")
                ))
                .from(project)
                .join(project.member)
                .where(project.id.in(projectIds))
                .fetch());

    }

    @Override
    public List<MemberRecommendationProjectDto> findAllByTechStackIdsAndDeadline(
            List<Long> techStackIds,
            LocalDate startDate,
            LocalDate endDate) {

        return new ArrayList<>(jpaQueryFactory
                .from(projectTechStack)
                .innerJoin(projectTechStack.project, project)
                .innerJoin(projectTechStack.techStack, techStack)
                .where(project.status.eq(ProjectStatus.RECRUITING),
                        (project.deadline.between(startDate, endDate)),
                        (techStack.id.in(techStackIds)),
                        (project.createdAt.before(LocalDateTime.of(startDate, LocalTime.MIN))))
                .limit(10)
                .transform(groupBy(project.id).list(
                        Projections.fields(MemberRecommendationProjectDto.class,
                                project.id.as("id"),
                                project.title.as("subject"),
                                project.deadline.as("deadline"),
                                list(techStack.name).as("techStackNames"))
                ))
        );
    }

    @Override
    public Optional<Project> findByIdWithMemberAndTechStack(Long projectId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(project)
                .where(project.id.eq(projectId))
                .leftJoin(project.member, member).fetchJoin()
                .leftJoin(project.projectTechStacks, projectTechStack).fetchJoin()
                .leftJoin(projectTechStack.techStack, techStack).fetchJoin()
                .fetchOne());
    }

    @Override
    public List<SimpleProjectDto> findAllOptionAndSearch(ProjectCondition projectCondition) {
        List<Long> projectIds = getMultiConditionSearchId(projectCondition);
        if (projectIds.isEmpty()) {
            return Collections.emptyList();
        }

        return findProjectAndTechStacksByIdList(projectIds);
    }

    private List<SimpleProjectDto> findProjectAndTechStacksByIdList(List<Long> projectIds) {
        return new ArrayList<>(jpaQueryFactory
                .from(project)
                .where(project.id.in(projectIds))
                .leftJoin(project.projectTechStacks, projectTechStack)
                .leftJoin(projectTechStack.techStack, techStack)
                .transform(GroupBy.groupBy(project.id)
                        .as(simpleProjectDto()))
                .values());
    }

    private QSimpleProjectDto simpleProjectDto() {
        return new QSimpleProjectDto(
                project.id,
                new QSimpleMemberDto(
                        project.member.id,
                        project.member.nickname,
                        project.member.profileImage),
                project.title,
                project.personnel,
                project.status,
                project.deadline,
                list(new QSimpleTechStackDto(
                        projectTechStack.techStack.id,
                        projectTechStack.techStack.name,
                        projectTechStack.techStack.image))
        );
    }

    /*
     *   페이징
     *   기술스택, 프로젝트상태, 필터링
     *   제목, 내용, 글쓴이 검색
     * */
    private List<Long> getMultiConditionSearchId(ProjectCondition projectCondition) {
        return jpaQueryFactory
                .select(project.id)
                .from(project)
                .leftJoin(project.projectTechStacks, projectTechStack)
                .leftJoin(project.member, member)
                .where(filterProjectStatus(projectCondition),
                        filterTechStacks(projectCondition),
                        searchProjectContent(projectCondition),
                        searchProjectTitle(projectCondition),
                        searchUserName(projectCondition))
                .distinct()
                .limit(projectCondition.getLimit())
                .offset(projectCondition.getPageNumber() * projectCondition.getLimit())
                .fetch();
    }

    private BooleanExpression filterProjectStatus(ProjectCondition projectCondition) {
        return projectCondition.getProjectStatus() == null ? null : project.status.eq(projectCondition.getProjectStatus());
    }

    private BooleanExpression filterTechStacks(ProjectCondition projectCondition) {
        return projectCondition.getTechStackIds() == null ? null : projectTechStack.techStack.id.in(projectCondition.getTechStackIds());
    }

    private BooleanExpression searchProjectContent(ProjectCondition projectCondition) {
        return projectCondition.getContent() == null ? null : project.content.contains(projectCondition.getContent());
    }

    private BooleanExpression searchProjectTitle(ProjectCondition projectCondition) {
        return projectCondition.getTitle() == null ? null : project.title.contains(projectCondition.getTitle());
    }

    private BooleanExpression searchUserName(ProjectCondition projectCondition) {
        return projectCondition.getAuthor() == null ? null : member.nickname.contains(projectCondition.getAuthor());
    }

}
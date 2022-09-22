package chocoteamteam.togather.repository.impl;


import chocoteamteam.togather.dto.ProjectCondition;
import chocoteamteam.togather.dto.queryDslSimpleDto.QSimpleMemberDto;
import chocoteamteam.togather.dto.queryDslSimpleDto.QSimpleProjectDto;
import chocoteamteam.togather.dto.queryDslSimpleDto.QSimpleTechStackDto;
import chocoteamteam.togather.dto.queryDslSimpleDto.SimpleProjectDto;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.repository.QueryDslProjectRepository;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static chocoteamteam.togather.entity.QMember.member;
import static chocoteamteam.togather.entity.QProject.project;
import static chocoteamteam.togather.entity.QProjectTechStack.projectTechStack;
import static chocoteamteam.togather.entity.QTechStack.techStack;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
@Repository
public class QueryDslProjectRepositoryImpl implements QueryDslProjectRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Project> findByIdWithMemberAndTechStack(Long projectId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(project)
                .where(project.id.eq(projectId))
                .leftJoin(project.member,member).fetchJoin()
                .leftJoin(project.projectTechStacks, projectTechStack).fetchJoin()
                .leftJoin(projectTechStack.techStack, techStack).fetchJoin()
                .fetchOne());
    }

    @Override
    public List<SimpleProjectDto> findAllOptionAndSearch(ProjectCondition projectCondition) {
        List<Long> projectIds = getMultiConditionSearchId(projectCondition);
        if (projectIds.size() == 0) {
            return Collections.emptyList();
        }

        return new ArrayList<>(jpaQueryFactory
                .from(project)
                .where(project.id.in(projectIds))
                .innerJoin(project.projectTechStacks, projectTechStack)
                .transform(GroupBy.groupBy(project.id)
                        .as(new QSimpleProjectDto(
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
                        )))
                .values());
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
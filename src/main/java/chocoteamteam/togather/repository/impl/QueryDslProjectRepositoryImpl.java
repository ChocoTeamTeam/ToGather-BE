package chocoteamteam.togather.repository.impl;


import chocoteamteam.togather.dto.ProjectCondition;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.repository.QueryDslProjectRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static chocoteamteam.togather.entity.QMember.member;
import static chocoteamteam.togather.entity.QProject.project;
import static chocoteamteam.togather.entity.QProjectTechStack.projectTechStack;
import static chocoteamteam.togather.entity.QTechStack.techStack;

@RequiredArgsConstructor
@Repository
public class QueryDslProjectRepositoryImpl implements QueryDslProjectRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Project> findByIdQuery(Long projectId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(project)
                .where(project.id.eq(projectId))
                .leftJoin(project.projectTechStacks, projectTechStack).fetchJoin()
                .leftJoin(projectTechStack.techStack, techStack).fetchJoin()
                .fetchOne());
    }

    /*
     *   페이징
     *   기술스택, 프로젝트상태, 필터링
     *   제목, 내용, 글쓴이 검색
     * */
    @Override
    public List<Project> findAllOptionAndSearch(ProjectCondition projectCondition) {
        return jpaQueryFactory
                .select(project)
                .from(project)
                .leftJoin(project.projectTechStacks, projectTechStack).fetchJoin()
                .leftJoin(project.member, member).fetchJoin()
                .leftJoin(projectTechStack.techStack, techStack).fetchJoin()
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
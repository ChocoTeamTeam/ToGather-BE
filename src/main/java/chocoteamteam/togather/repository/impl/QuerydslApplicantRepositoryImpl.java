package chocoteamteam.togather.repository.impl;

import static chocoteamteam.togather.entity.QApplicant.applicant;
import static chocoteamteam.togather.entity.QMember.member;
import static chocoteamteam.togather.entity.QMemberTechStack.memberTechStack;
import static chocoteamteam.togather.entity.QTechStack.techStack;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.types.dsl.Expressions.*;

import chocoteamteam.togather.dto.ApplicantDto;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.repository.QuerydslApplicantRepository;
import chocoteamteam.togather.type.ApplicantStatus;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Repository
@Transactional
public class QuerydslApplicantRepositoryImpl implements QuerydslApplicantRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ApplicantDto> findAllByProjectId(Long projectId, ApplicantStatus status) {
		return jpaQueryFactory.selectFrom(applicant)
			.innerJoin(applicant.member, member)
			.innerJoin(member.memberTechStacks, memberTechStack)
			.innerJoin(memberTechStack.techStack, techStack)
			.where(applicant.project.id.eq(projectId), applicant.status.eq(status))
			.transform(groupBy(applicant.id).list(
				Projections.fields(
					ApplicantDto.class,
					applicant.id.as("applicantId"),
					asNumber(projectId).as("projectId"),
					member.id.as("memberId"),
					member.nickname,
					member.profileImage,
					list(Projections.fields(
							TechStackDto.class,
							techStack.id,
							techStack.name,
							techStack.category,
							techStack.image
						)
					).as("techStacks")
				)
			));
	}

}

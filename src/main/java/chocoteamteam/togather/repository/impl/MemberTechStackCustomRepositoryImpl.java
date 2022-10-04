package chocoteamteam.togather.repository.impl;

import static chocoteamteam.togather.entity.QMember.member;
import static chocoteamteam.togather.entity.QMemberTechStack.memberTechStack;
import static chocoteamteam.togather.entity.QTechStack.techStack;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.repository.MemberTechStackCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberTechStackCustomRepositoryImpl implements MemberTechStackCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public MemberDetailResponse findMemberWithTechStackDetailByMemberId(Long memberId) {
        return queryFactory
            .from(memberTechStack)
            .where(memberTechStack.member.id.in(memberId))
            .join(memberTechStack.member, member)
            .join(memberTechStack.techStack, techStack)
            .transform(groupBy(memberTechStack.member.id)
                .as(Projections.constructor(
                    MemberDetailResponse.class,
                    memberTechStack.member.id,
                    memberTechStack.member.nickname,
                    memberTechStack.member.profileImage,
                    list(Projections.constructor(
                            TechStackDto.class,
                            memberTechStack.techStack.id,
                            memberTechStack.techStack.name,
                            memberTechStack.techStack.category,
                            memberTechStack.techStack.image
                        )
                    )
                ))).get(memberId);
    }

}

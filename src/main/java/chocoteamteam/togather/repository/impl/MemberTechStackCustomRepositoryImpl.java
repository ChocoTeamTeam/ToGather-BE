package chocoteamteam.togather.repository.impl;

import chocoteamteam.togather.dto.queryDslSimpleDto.MemberTechStackInfoDto;
import chocoteamteam.togather.entity.QMemberTechStack;
import chocoteamteam.togather.repository.MemberTechStackCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberTechStackCustomRepositoryImpl implements MemberTechStackCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberTechStackInfoDto> findAllByMemberId(Long id) {
        QMemberTechStack memberTechStack = QMemberTechStack.memberTechStack;
        return queryFactory
            .select(Projections.constructor(MemberTechStackInfoDto.class,
                memberTechStack.member.id,
                memberTechStack.member.nickname,
                memberTechStack.member.profileImage,
                memberTechStack.techStack.id,
                memberTechStack.techStack.name,
                memberTechStack.techStack.image
            ))
            .from(memberTechStack)
            .join(memberTechStack.member)
            .join(memberTechStack.techStack)
            .where(memberTechStack.member.id.eq(id))
            .fetch();
    }
}

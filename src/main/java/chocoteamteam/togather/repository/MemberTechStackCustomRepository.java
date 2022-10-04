package chocoteamteam.togather.repository;

import chocoteamteam.togather.dto.MemberDetailResponse;

public interface MemberTechStackCustomRepository {

    MemberDetailResponse findMemberWithTechStackDetailByMemberId(Long memberId);

}

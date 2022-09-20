package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.dto.queryDslSimpleDto.MemberTechStackInfoDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackCustomRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TechStackRepository techStackRepository;

    private final MemberTechStackCustomRepository memberTechStackCustomRepository;


    @Transactional(readOnly = true)
    public MemberDetailResponse getDetail(Long memberId) {

        List<MemberTechStackInfoDto> memberTechStackInfoDtos =
            memberTechStackCustomRepository.findAllByMemberId(memberId);

        if (memberTechStackInfoDtos.isEmpty()) {
            Member member = getMember(memberId);
            return MemberDetailResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .build();
        }

        return MemberDetailResponse.fromMemberTechStackInfoDtos(memberTechStackInfoDtos);

    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
    }
}

package chocoteamteam.togather.service;

import static chocoteamteam.togather.exception.ErrorCode.NOT_FOUND_MEMBER;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.queryDslSimpleDto.MemberTechStackInfoDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackCustomRepository;
import chocoteamteam.togather.repository.RefreshTokenRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.MemberStatus;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
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

	@Transactional
	public void changeStatus(@NonNull Long memberId, @NonNull MemberStatus status) {
		Member member = getMember(memberId);

		member.changeStatus(status);

		refreshTokenRepository.delete(memberId);
	}

}

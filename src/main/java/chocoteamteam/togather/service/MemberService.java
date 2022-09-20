package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.SignUpControllerDto.Request;
import chocoteamteam.togather.dto.queryDslSimpleDto.MemberTechStackInfoDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.MemberTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackCustomRepository;
import chocoteamteam.togather.repository.MemberTechStackRepository;
import chocoteamteam.togather.repository.RefreshTokenRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.MemberStatus;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TechStackRepository techStackRepository;
    private final MemberTechStackRepository memberTechStackRepository;
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

    @Transactional
    public void modify(Long memberId, Request request, Long loginId) {

        //  로그인한 회원 Id와 요청을 보낸 회원 Id가 다를 경우 예외 발생
        if (!Objects.equals(loginId, memberId)) {
            throw new MemberException(ErrorCode.MISS_MATCH_MEMBER);
        }

        /*  닉네임으로 회원을 가져와 회원이 존재할 경우
            해당 회원 Id과 현재 수정을 요청한 회원 Id를 비교해 다를 경우 예외 발생
         */
        Optional<Member> byNickname = memberRepository.findByNickname(request.getNickname());
        if (byNickname.isPresent() && !Objects.equals(byNickname.get().getId(), memberId)) {
            throw new MemberException(ErrorCode.EXIST_TRUE_MEMBER_NICKNAME);
        }

        //  회원과 회원이 가지고 있는 기술 스택 한 번에 가져옴 회원이 존재하지 않을 경우 예외 발생
        Member member = memberRepository.findWithMemberTechStackById(memberId)
            .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        member.modifyNicknameAndProfileImage(request.getNickname(), request.getProfileImage());

        // 삭제할 MemberTechStackId와 저장할 TechStackId를 구분
        List<MemberTechStack> memberTechStacks = member.getMemberTechStacks();
        Set<Long> requestTechStackIds = new HashSet<>(request.getTechStackDtos());
        Set<Long> deleteMemberTechStackIds = new HashSet<>();

        for (MemberTechStack memberTechStack : memberTechStacks) {
            Long curMemberTechStackId = memberTechStack.getTechStack().getId();
            if (requestTechStackIds.contains(curMemberTechStackId)) {
                requestTechStackIds.remove(curMemberTechStackId);
                continue;
            }
            deleteMemberTechStackIds.add(curMemberTechStackId);
        }

        // 삭제할 MemberTechStackId List가 비어있지 않다면 삭제
        if (!deleteMemberTechStackIds.isEmpty()) {
            memberTechStackRepository.deleteAllByIdInQuery(deleteMemberTechStackIds);
        }

        // 저장할 TechStackId List가 비어있지 않다면 저장
        if (!requestTechStackIds.isEmpty()) {
            List<TechStack> techStacks = techStackRepository.findAllById(requestTechStackIds);
            for (TechStack techStack : techStacks) {
                memberTechStackRepository.save(MemberTechStack.builder()
                    .member(member)
                    .techStack(techStack)
                    .build());
            }
        }

    }

}

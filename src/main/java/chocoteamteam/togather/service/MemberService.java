package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.SignUpControllerDto.Request;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.MemberTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackRepository;
import chocoteamteam.togather.repository.RefreshTokenRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.MemberStatus;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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


    @Transactional(readOnly = true)
    public MemberDetailResponse getDetail(Long memberId) {

        MemberDetailResponse memberDetailResponse =
            memberTechStackRepository.findMemberWithTechStackDetailByMemberId(memberId);

        if (memberDetailResponse == null) {
            Member member = getMember(memberId);
            return MemberDetailResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .build();
        }

        return memberDetailResponse;
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
    public MemberDetailResponse modify(Long memberId, Request request, Long loginId) {

        //  ???????????? ?????? Id??? ????????? ?????? ?????? Id??? ?????? ?????? ?????? ??????
        if (!Objects.equals(loginId, memberId)) {
            throw new MemberException(ErrorCode.MISS_MATCH_MEMBER);
        }

        /*  ??????????????? ????????? ????????? ????????? ????????? ??????
            ?????? ?????? Id??? ?????? ????????? ????????? ?????? Id??? ????????? ?????? ?????? ?????? ??????
         */
        checkMemberIdAndNickname(request.getNickname(), memberId);

        //  ????????? ????????? ????????? ?????? ?????? ?????? ??? ?????? ????????? ????????? ???????????? ?????? ?????? ?????? ??????
        Member member = memberRepository.findWithMemberTechStackById(memberId)
            .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        // ?????? ?????? ??????
        member.modifyNicknameAndProfileImage(request.getNickname(), request.getProfileImage());

        // ????????? MemberTechStackId??? ????????? TechStackId??? ??????
        List<MemberTechStack> memberTechStacks = member.getMemberTechStacks();
        Set<Long> requestTechStackIds = request.getTechStackDtos().stream()
            .map(TechStackDto::getId)
            .collect(Collectors.toSet());
        Set<Long> deleteMemberTechStackIds = new HashSet<>();
        initMemberTechStackDeleteAndInsertIds(
            memberTechStacks,
            requestTechStackIds,
            deleteMemberTechStackIds
        );

        // ????????? MemberTechStackId List ???????????? ????????? ??????
        deleteByMemberTechStackIds(deleteMemberTechStackIds);

        // ????????? TechStackId List ???????????? ????????? ??????
        saveByMemberTechStack(member, requestTechStackIds);

        return MemberDetailResponse.builder()
            .id(memberId)
            .nickname(request.getNickname())
            .profileImage(request.getProfileImage())
            .techStackDtos(request.getTechStackDtos())
            .build();
    }

    private void initMemberTechStackDeleteAndInsertIds(List<MemberTechStack> memberTechStacks,
        Set<Long> insertIds,
        Set<Long> deleteIds) {
        for (MemberTechStack memberTechStack : memberTechStacks) {
            Long curMemberTechStackId = memberTechStack.getTechStack().getId();
            if (insertIds.contains(curMemberTechStackId)) {
                insertIds.remove(curMemberTechStackId);
                continue;
            }
            deleteIds.add(curMemberTechStackId);
        }
    }

    private void checkMemberIdAndNickname(String nickname, Long memberId) {
        Optional<Member> byNickname = memberRepository.findByNickname(nickname);
        if (byNickname.isPresent() && !Objects.equals(byNickname.get().getId(), memberId)) {
            throw new MemberException(ErrorCode.EXIST_TRUE_MEMBER_NICKNAME);
        }

    }

    private void saveByMemberTechStack(Member member, Set<Long> techStackIds) {
        if (!techStackIds.isEmpty()) {
            List<TechStack> techStacks = techStackRepository.findAllById(techStackIds);
            for (TechStack techStack : techStacks) {
                memberTechStackRepository.save(MemberTechStack.builder()
                    .member(member)
                    .techStack(techStack)
                    .build());
            }
        }
    }

    private void deleteByMemberTechStackIds(Set<Long> ids) {
        if (!ids.isEmpty()) {
            memberTechStackRepository.deleteAllByIdInQuery(ids);
        }
    }

    public boolean existNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}

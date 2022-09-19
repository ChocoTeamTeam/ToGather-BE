package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TechStackRepository techStackRepository;


    @Transactional
    public MemberDetailResponse getDetail(Long memberId) {

        Member member = getMember(memberId);

        List<Long> techStackIds = member.getMemberTechStacks().stream()
            .map(memberTechStack -> memberTechStack.getTechStack().getId())
            .collect(Collectors.toList());

        List<TechStackDto> techStackDtos = techStackRepository.findAllById(techStackIds).stream()
            .map(TechStackDto::from)
            .collect(Collectors.toList());

        return MemberDetailResponse.builder()
            .id(member.getId())
            .profileImage(member.getProfileImage())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .techStackDtos(techStackDtos)
            .build();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));
    }
}

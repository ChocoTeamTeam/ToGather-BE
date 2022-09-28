package chocoteamteam.togather.service;

import chocoteamteam.togather.entity.Interest;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.InterestException;
import chocoteamteam.togather.repository.InterestRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    public void add(Long memberId, Long projectId, Long loginMemberId) {

        checkRequestMemberIdAndLoginMemberId(memberId, loginMemberId);

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new InterestException(ErrorCode.NOT_FOUND_MEMBER));
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new InterestException(ErrorCode.NOT_FOUND_PROJECT));

        interestRepository.save(Interest.builder()
            .member(member)
            .project(project)
            .build());

    }

    private void checkRequestMemberIdAndLoginMemberId(Long requestMemberId, Long loginMemberId) {
        if (!Objects.equals(requestMemberId, loginMemberId)) {
            throw new InterestException(ErrorCode.NO_PERMISSION);
        }
    }
}

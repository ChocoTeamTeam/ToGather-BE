package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.InterestDetail;
import chocoteamteam.togather.entity.Interest;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.InterestException;
import chocoteamteam.togather.repository.InterestRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    private static final int MAX_INTEREST = 5;

    @Transactional
    public String addOrRemove(Long projectId, Long loginMemberId) {

        Optional<Interest> interestOptional = interestRepository.findByMemberIdAndProjectId(
            loginMemberId, projectId);

        if (interestOptional.isPresent()) {
            remove(interestOptional.get());
            return "remove";
        }

        if (interestRepository.countByMemberId(loginMemberId) >= MAX_INTEREST) {
            throw new InterestException(ErrorCode.MAXIMUM_PROJECT_INTEREST);
        }

        add(loginMemberId, projectId);
        return "add";

    }

    private void add(Long memberId, Long projectId) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new InterestException(ErrorCode.NOT_FOUND_MEMBER));
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new InterestException(ErrorCode.NOT_FOUND_PROJECT));

        interestRepository.save(Interest.builder()
            .member(member)
            .project(project)
            .build());

    }

    @Transactional(readOnly = true)
    public List<InterestDetail> getDetails(Long loginMemberId) {

        List<Long> projectIds = interestRepository.findAllByMemberId(loginMemberId).stream()
            .map(e -> e.getProject().getId()).collect(Collectors.toList());

        if (projectIds.isEmpty()) {
            return Collections.emptyList();
        }

        return projectRepository.findAllInterestProjectByIds(projectIds);
    }

    private void remove(Interest interest) {
        interestRepository.delete(interest);
    }

}

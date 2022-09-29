package chocoteamteam.togather.service;

import chocoteamteam.togather.entity.Interest;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.InterestException;
import chocoteamteam.togather.repository.InterestRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public String addOrRemove(Long projectId, Long loginMemberId) {

        Optional<Interest> interestOptional = interestRepository.findByMemberIdAndProjectId(
            loginMemberId, projectId);

        if (interestOptional.isPresent()) {
            remove(interestOptional.get());
            return "remove";
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

    private void remove(Interest interest) {
        interestRepository.delete(interest);
    }

}

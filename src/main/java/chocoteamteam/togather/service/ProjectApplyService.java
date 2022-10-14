package chocoteamteam.togather.service;

import chocoteamteam.togather.entity.Applicant;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ApplicantException;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.repository.ApplicantRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.type.ApplicantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProjectApplyService {

	private final ProjectRepository projectRepository;
	private final ApplicantRepository applicantRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void applyProject(Long memberId, Long projectId) {
		validateApplicant(projectId,memberId);
		saveApplicant(projectId, memberId);
	}

	private void validateApplicant(Long memberId, Long projectId) {
		if (applicantRepository.existsByProjectIdAndMemberId(projectId, memberId)) {
			throw new ApplicantException(ErrorCode.ALREADY_APPLY_PROJECT);
		}
	}

	private void saveApplicant(Long memberId, Long projectId) {
		applicantRepository.save(Applicant.builder()
			.project(projectRepository.getReferenceById(projectId))
			.member(memberRepository.getReferenceById(memberId))
			.status(ApplicantStatus.WAIT)
			.build());
	}




}

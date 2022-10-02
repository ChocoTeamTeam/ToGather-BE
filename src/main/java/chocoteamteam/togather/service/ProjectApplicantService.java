package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.ApplicantDto;
import chocoteamteam.togather.dto.ManageApplicantForm;
import chocoteamteam.togather.entity.Applicant;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ApplicantException;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.ProjectException;
import chocoteamteam.togather.repository.ApplicantRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.type.ApplicantStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProjectApplicantService {

	private final ProjectRepository projectRepository;
	private final ApplicantRepository applicantRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void applyForProject(Long memberId, Long projectId) {
		validateApplicant(projectId, memberId);
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

	@Transactional(readOnly = true)
	public List<ApplicantDto> getApplicants(Long projectId, Long memberId) {
		validateProjectOwnerByProjectId(memberId, projectId);

		return applicantRepository.findAllByProjectId(projectId,
			ApplicantStatus.WAIT);
	}

	private void validateProjectOwnerByProjectId(Long memberId, Long projectId) {
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new ProjectException(ErrorCode.NOT_FOUND_PROJECT));

		if (!project.getMember().getId().equals(memberId)) {
			throw new ProjectException(ErrorCode.NOT_MATCH_MEMBER_PROJECT);
		}
	}

	@Transactional
	public void manageApplicant(ManageApplicantForm form) {
		validateProjectOwnerByProjectId(form.getProjectOwnerMemberId(), form.getProjectId());

		Applicant applicant = findApplicant(form.getProjectId(),form.getApplicantMemberId());

		applicant.changeStatus(form.getStatus());
	}

	@Transactional
	public void deleteApplicant(Long projectId, Long memberId) {
		Applicant applicant = findApplicant(projectId, memberId);

		applicantRepository.delete(applicant);
	}

	private Applicant findApplicant(Long projectId, Long memberId) {
		return applicantRepository.findByProjectIdAndMemberId(projectId, memberId)
			.orElseThrow(() -> new ApplicantException(ErrorCode.NOT_FOUND_APPLICANT));
	}

}

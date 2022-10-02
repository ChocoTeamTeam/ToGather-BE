package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.ApplicantDto;
import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.dto.ManageApplicantForm;
import chocoteamteam.togather.service.ProjectApplicantService;
import chocoteamteam.togather.type.ApplicantStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Tag(name = "Project_Applicant", description = "프로젝트 신청 관리 관련 API")
@RequiredArgsConstructor
@RequestMapping("/projects")
@RestController
public class ProjectApplicantController {

	private final ProjectApplicantService projectApplicantService;

	@Operation(
		summary = "프로젝트 참여 신청",
		description = "프로젝트에 참여 신청합니다.",
		security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project_Applicant"}
	)
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/{projectId}/applicants")
	public ResponseEntity applyProject(
		@ApiIgnore @AuthenticationPrincipal LoginMember member,
		@PathVariable Long projectId
	) {
		projectApplicantService.applyForProject(projectId, member.getId());

		return ResponseEntity.ok().body("");
	}

	@Operation(
		summary = "프로젝트 신청자 리스트 조회 ",
		description = "프로젝트에 신청자들을 조회합니다. WAIT 상태인 신청자만 조회됩니다.",
		security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project_Applicant"}
	)
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/{projectId}/applicants")
	public ResponseEntity<List<ApplicantDto>> getApplicants(
		@ApiIgnore @AuthenticationPrincipal LoginMember member, @PathVariable Long projectId) {

		return ResponseEntity.ok()
			.body(projectApplicantService.getApplicants(projectId, member.getId()));
	}


	@Operation(
		summary = "프로젝트 신청자 수락",
		description = "프로젝트 신청자의 신청을 수락합니다.",
		security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project_Applicant"}
	)
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/{projectId}/applicants/{memberId}/accept")
	public ResponseEntity acceptApplicant(
		@PathVariable Long projectId, @PathVariable Long memberId,
		@AuthenticationPrincipal LoginMember member) {

		projectApplicantService.manageApplicant(ManageApplicantForm.builder()
			.projectId(projectId)
			.applicantMemberId(memberId)
			.projectOwnerMemberId(member.getId())
			.status(ApplicantStatus.ACCEPTED)
			.build());

		return ResponseEntity.ok().body("");
	}


	@Operation(
		summary = "프로젝트 신청자 거절",
		description = "프로젝트에 신청자의 신청을 거절합니다.",
		security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project_Applicant"}
	)
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/{projectId}/applicants/{memberId}/reject")
	public ResponseEntity rejectApplicant(
		@PathVariable Long projectId, @PathVariable Long memberId,
		@AuthenticationPrincipal LoginMember member) {

		projectApplicantService.manageApplicant(ManageApplicantForm.builder()
			.projectId(projectId)
			.applicantMemberId(memberId)
			.projectOwnerMemberId(member.getId())
			.status(ApplicantStatus.ACCEPTED)
			.build());

		return ResponseEntity.ok().body("");
	}


	@Operation(
		summary = "프로젝트 신청자 취소",
		description = "프로젝트에 신청자를 삭제합니다.",
		security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project_Applicant"}
	)
	@PreAuthorize("hasRole('USER') and principal.id == #memberId or hasRole('ADMIN')")
	@DeleteMapping("/{projectId}/applicants/{memberId}")
	public ResponseEntity deleteApplicant(
		@PathVariable Long projectId, @PathVariable Long memberId) {

		projectApplicantService.deleteApplicant(projectId, memberId);

		return ResponseEntity.ok().body("");
	}

}

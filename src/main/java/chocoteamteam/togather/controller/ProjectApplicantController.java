package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.ApplicantDto;
import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.service.ProjectApplicantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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




}

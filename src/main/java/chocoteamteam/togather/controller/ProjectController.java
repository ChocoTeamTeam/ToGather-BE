package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.*;
import chocoteamteam.togather.service.ProjectApplicantService;
import chocoteamteam.togather.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import springfox.documentation.annotations.ApiIgnore;

@Tag(name = "Project", description = "프로젝트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
	private final ProjectApplicantService projectApplicantService;

    @Operation(
            summary = "프로젝트 모집글 등록",
            description = "프로젝트 모집글을 등록합니다. 로그인 하지 않은 유저는 불가능합니다",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project"}
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @AuthenticationPrincipal LoginMember member,
            @Valid @RequestBody CreateProjectForm form
    ) {
        return ResponseEntity.ok(
                projectService.createProject(member.getId(), form)
        );
    }

    @Operation(
            summary = "프로젝트 모집글 수정",
            description = "프로젝트 모집글을 수정합니다. 본인이 쓴 글만 수정 가능합니다",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project"}
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @AuthenticationPrincipal LoginMember member,
            @PathVariable Long projectId,
            @Valid @RequestBody UpdateProjectForm form
    ) {
        return ResponseEntity.ok(
                projectService.updateProject(projectId, member.getId(), form)
        );
    }

    @Operation(
            summary = "프로젝트 모집글 목록 조회",
            description = "프로젝트 모집글 목록을 조회합니다",
            tags = {"Project"}
    )
    @GetMapping
    public ResponseEntity<?> getProjectList(@Valid ProjectCondition projectCondition) {
        return ResponseEntity.ok(projectService.getProjectList(projectCondition));
    }

    @Operation(
            summary = "프로젝트 모집글 상세 조회",
            description = "특정 프로젝트 모집글을 상세 조회합니다",
            tags = {"Project"}
    )
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetails> getProjectDetail(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProject(projectId));
    }

    @Operation(
            summary = "프로젝트 모집글 삭제",
            description = "프로젝트 모집글을 삭제합니다. 일반 회원은 본인이 쓴 글만 삭제가능하며 ADMIN은 전부 삭제 가능합니다",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project"}
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ProjectDto> deleteProject(
            @AuthenticationPrincipal LoginMember member,
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(projectService.deleteProject(projectId, member.getId(), member.getRole()));
    }

    @Operation(
            summary = "내가 올린 프로젝트 목록 조회",
            description = "내가 올린 프로젝트 모집글 목록을 조회합니다",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project"}
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/myProjects")
    public ResponseEntity<?> getMyProjects(@AuthenticationPrincipal LoginMember member) {
        return ResponseEntity.ok(projectService.getMyProjects(member.getId()));
    }

    @Operation(
            summary = "내가 참여중인 프로젝트 목록 조회",
            description = "내가 참여중인 프로젝트 모집글 목록을 조회합니다",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project"}
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/participatingProjects")
    public ResponseEntity<?> getMyParticipatingProjects(@AuthenticationPrincipal LoginMember member) {
        return ResponseEntity.ok(projectService.getMyParticipatingProjects(member.getId()));
    }

    @Operation(
            summary = "거리에 따른 프로젝트 목록 조회",
            description = "특정 거리 안에 있는 프로젝트 모집글 목록을 조회합니다",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"Project"}
    )
    @GetMapping("/search/distance")
    public ResponseEntity<?> getProjectByDistance(@Valid ProjectDistance projectDistance) {
        return ResponseEntity.ok(projectService.getProjectByDistance(projectDistance));
    }
}
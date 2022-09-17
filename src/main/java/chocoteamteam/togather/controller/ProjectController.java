package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.CreateProjectForm;
import chocoteamteam.togather.dto.ProjectDto;
import chocoteamteam.togather.dto.UpdateProjectForm;
import chocoteamteam.togather.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    // 나중에 memberId 받아오기
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody CreateProjectForm form
    ) {
        Long memberId = 1L;
        return ResponseEntity.ok(
                projectService.createProject(memberId, form)
        );
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody UpdateProjectForm form
    ) {
        Long memberId = 1L;
        return ResponseEntity.ok(
                projectService.updateProject(projectId, memberId, form)
        );
    }
}
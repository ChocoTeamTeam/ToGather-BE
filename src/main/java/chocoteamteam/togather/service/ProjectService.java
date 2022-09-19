package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.*;
import chocoteamteam.togather.dto.queryDslSimpleDto.SimpleProjectDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.entity.ProjectTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.ProjectException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.repository.ProjectTechStackRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.ProjectStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static chocoteamteam.togather.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final TechStackRepository techStackRepository;
    private final ProjectTechStackRepository projectTechStackRepository;

    @Transactional
    public ProjectDto createProject(Long memberId, CreateProjectForm form) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ProjectException(NOT_FOUND_MEMBER));
        Project project = projectRepository.save(Project.builder()
                .member(member)
                .title(form.getTitle())
                .content(form.getContent())
                .personnel(form.getPersonnel())
                .status(ProjectStatus.RECRUITING)
                .offline(form.getOffline())
                .location(form.getLocation())
                .deadline(form.getDeadline())
                .build());

        saveProjectTechs(project, getTechStacks(form.getTechStackIds()));
        return ProjectDto.from(project);
    }

    private List<TechStack> getTechStacks(List<Long> techStackIds) {
        List<TechStack> techStacks = techStackRepository.findAllById(techStackIds);

        if (techStacks.size() != techStackIds.size()) {
            throw new ProjectException(NOT_FOUND_TECH_STACK);
        }

        return techStacks;
    }

    private void saveProjectTechs(Project project, List<TechStack> techStacks) {

        List<ProjectTechStack> projectTechStacks = new ArrayList<>();
        for (TechStack tech : techStacks) {
            projectTechStacks.add(new ProjectTechStack(project, tech));
        }
        projectTechStackRepository.saveAll(projectTechStacks);
    }

    @Transactional
    public ProjectDto updateProject(
            Long projectId,
            Long memberId,
            UpdateProjectForm form
    ) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(NOT_FOUND_PROJECT));
        if (!Objects.equals(project.getMember().getId(), memberId)) {
            throw new ProjectException(NOT_MATCH_MEMBER_PROJECT);
        }

        project.update(form);
        projectTechStackRepository.deleteAllByProjectId(project.getId());
        projectTechStackRepository.flush();

        project.getProjectTechStacks().clear();
        saveProjectTechs(project, getTechStacks(form.getTechStackIds()));
        return ProjectDto.from(project);
    }

    public List<SimpleProjectDto> getProjectList(ProjectCondition projectCondition) {
        return projectRepository.findAllOptionAndSearch(projectCondition);
    }

    public ProjectDetails getProject(Long projectId) {
        return ProjectDetails.fromEntity(projectRepository.findByIdQuery(projectId)
                .orElseThrow(() -> new ProjectException(NOT_FOUND_PROJECT)));
    }
}
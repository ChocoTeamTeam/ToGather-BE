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
import java.util.*;

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
        Project project = getValidProject(projectId, memberId);
        project.update(form);
        calcAndUpdateTechStack(projectId, form, project);
        return ProjectDto.from(project);
    }

    private Project getValidProject(Long projectId, Long memberId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(NOT_FOUND_PROJECT));

        if (!Objects.equals(project.getMember().getId(), memberId)) {
            throw new ProjectException(NOT_MATCH_MEMBER_PROJECT);
        }
        return project;
    }

    private void calcAndUpdateTechStack(Long projectId, UpdateProjectForm form, Project project) {
        Set<Long> prevIds = new HashSet<>(projectTechStackRepository.findTechStackIdsByProjectId(projectId));
        Set<Long> deleteIds = new HashSet<>(prevIds);
        Set<Long> addIds = new HashSet<>(form.getTechStackIds());
        deleteIds.removeAll(addIds);
        addIds.removeAll(prevIds);

        updateProjectTechStack(project, deleteIds, addIds);
    }

    private void updateProjectTechStack(Project project, Set<Long> deleteIds, Set<Long> addIds) {
        if (deleteIds.size() > 0) {
            List<ProjectTechStack> projectTechStacks =
                    projectTechStackRepository.deleteAllByIdIn(new ArrayList<>(deleteIds));
            project.getProjectTechStacks().removeAll(projectTechStacks);
        }

        if (addIds.size() > 0) {
            saveProjectTechs(project, getTechStacks(new ArrayList<>(addIds)));
        }
    }


    public List<SimpleProjectDto> getProjectList(ProjectCondition projectCondition) {
        return projectRepository.findAllOptionAndSearch(projectCondition);
    }

    public ProjectDetails getProject(Long projectId) {
        return ProjectDetails.fromEntity(projectRepository.findByIdQuery(projectId)
                .orElseThrow(() -> new ProjectException(NOT_FOUND_PROJECT)));
    }
}
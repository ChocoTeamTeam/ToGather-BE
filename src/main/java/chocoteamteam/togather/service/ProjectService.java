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
import chocoteamteam.togather.type.Role;
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

        //프로젝트, 멤버, 모집기술스택, 기술스택 한번에 가져옴
        Project project = projectRepository.findByIdQuery(projectId)
                .orElseThrow(() -> new ProjectException(NOT_FOUND_PROJECT));
        validate(project, memberId);
        return updateProject(form, project);
    }

    private void validate(Project project, Long memberId) {
        if (!Objects.equals(project.getMember().getId(), memberId)) {
            throw new ProjectException(NOT_MATCH_MEMBER_PROJECT);
        }
    }

    private ProjectDto updateProject(UpdateProjectForm form, Project project) {
        project.update(form);
        calcAndUpdateTechStack(project, form);
        return ProjectDto.from(project);
    }

    private void calcAndUpdateTechStack(Project project, UpdateProjectForm form) {
        List<ProjectTechStack> prevProjectTechStacks = project.getProjectTechStacks();

        //삭제 되어야 할 모집 기술 스택 id
        List<Long> deleteIds = new ArrayList<>();
        //추가 할 기술 스택 id
        Set<Long> addTechIds = new HashSet<>(form.getTechStackIds());
        //삭제 되어야 할 모집 기술 스택
        List<ProjectTechStack> deleteProjectTechStacks = new ArrayList<>();

        for (ProjectTechStack pt : prevProjectTechStacks) {
            //기존 기술 스택 id
            Long prevTechId = pt.getTechStack().getId();
            if (addTechIds.contains(prevTechId)) {
                addTechIds.remove(prevTechId);
            } else {
                deleteIds.add(pt.getId());
                deleteProjectTechStacks.add(pt);
            }
        }
        updateProjectTechStack(project, deleteIds, addTechIds, deleteProjectTechStacks);
    }

    private void updateProjectTechStack(
            Project project,
            List<Long> deleteIds,
            Set<Long> addIds,
            List<ProjectTechStack> deleteProjectTechStacks
    ) {

        if (!deleteIds.isEmpty()) {
            projectTechStackRepository.deleteAllByIdInQuery(deleteIds);
            project.getProjectTechStacks().removeAll(deleteProjectTechStacks);
        }

        if (!addIds.isEmpty()) {
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

    @Transactional
    public ProjectDto deleteProject(Long projectId, Long memberId, Role role) {
        Project project = projectRepository.findByIdQuery(projectId)
                .orElseThrow(() -> new ProjectException(NOT_FOUND_PROJECT));

        if (role != Role.ROLE_ADMIN) {
            validate(project, memberId);
        }

        return deleteProject(project);
    }
    private ProjectDto deleteProject(Project project) {
        projectTechStackRepository.deleteByProjectId(project.getId());
        projectRepository.deleteById(project.getId());
        return ProjectDto.from(project);
    }
}
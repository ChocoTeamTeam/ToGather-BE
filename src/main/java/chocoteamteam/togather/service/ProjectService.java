package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CreateProjectForm;
import chocoteamteam.togather.dto.ProjectDto;
import chocoteamteam.togather.dto.UpdateProjectForm;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.entity.ProjectTech;
import chocoteamteam.togather.exception.CustomException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.repository.ProjectTechRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static chocoteamteam.togather.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final TechStackRepository techStackRepository;
    private final ProjectTechRepository projectTechRepository;

    @Transactional
    public ProjectDto createProject(Long memberId, CreateProjectForm form) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

        Project project = Project.of(member, form);

        List<ProjectTech> projectTechs = getProjectTechs(form.getTechStackIds());
        projectTechs.forEach(pt -> pt.setProject(project));

        projectTechRepository.saveAll(projectTechs);
        projectRepository.save(project);

        return ProjectDto.from(project);
    }

    private List<ProjectTech> getProjectTechs(List<Long> techStackIds) {
        return techStackIds.stream()
                .map(tid -> techStackRepository.findById(tid)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_TECH_STACK)))
                .map(
                        tech -> ProjectTech.builder()
                                .techStack(tech)
                                .build()
                ).collect(Collectors.toList());
    }

    @Transactional
    public ProjectDto updateProject(
            Long projectId,
            Long memberId,
            UpdateProjectForm form
    ) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PROJECT));
        if (!Objects.equals(project.getMember().getId(), memberId)) {
            throw new CustomException(NOT_MATCH_MEMBER_PROJECT);
        }

        List<ProjectTech> projectTechs = getProjectTechs(form.getTechStackIds());
        project.setProjectTechs(new ArrayList<>());
        projectTechs.forEach(pt -> pt.setProject(project));
        project.update(form, projectTechs);
        projectTechRepository.deleteAllByProjectId(project.getId());
        projectTechRepository.saveAll(projectTechs);
        return ProjectDto.from(project);
    }
}

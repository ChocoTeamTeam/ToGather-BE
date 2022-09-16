package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CreateProjectForm;
import chocoteamteam.togather.dto.ProjectDto;
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
import java.util.List;
import java.util.stream.Collectors;

import static chocoteamteam.togather.exception.ErrorCode.NOT_FOUND_MEMBER;
import static chocoteamteam.togather.exception.ErrorCode.NOT_FOUND_TECH_STACK;

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
                .location(form.getLocation())
                .deadline(form.getDeadline())
                .build());

        saveProjectTechs(project, getTechStacks(form.getTechStackIds()));
        return ProjectDto.from(project);
    }

    private List<TechStack> getTechStacks(List<Long> techStackIds) {
        return techStackIds.stream()
                .map(tid -> techStackRepository.findById(tid)
                        .orElseThrow(() -> new ProjectException(NOT_FOUND_TECH_STACK)))
                .collect(Collectors.toList());
    }

    private void saveProjectTechs(Project project, List<TechStack> techStacks) {
        List<ProjectTechStack> projectTechStacks = techStacks.stream()
                .map(tech -> ProjectTechStack.builder()
                        .techStack(tech)
                        .build()
                ).collect(Collectors.toList());

        projectTechStacks.forEach(pt -> pt.setProject(project));
        projectTechStackRepository.saveAll(projectTechStacks);
    }

}
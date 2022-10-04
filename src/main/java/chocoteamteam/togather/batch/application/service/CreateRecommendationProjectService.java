package chocoteamteam.togather.batch.application.service;

import chocoteamteam.togather.batch.application.model.MemberRecommendationProjectDto;
import chocoteamteam.togather.batch.application.model.SimpleMemberTechStackInfoDto;
import chocoteamteam.togather.batch.domain.entity.Mail;
import chocoteamteam.togather.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CreateRecommendationProjectService {

    private final ProjectRepository projectRepository;
    private final TemplateEngine template;

    private final static String TEMPLATE_URL = "mail/recommend_project";
    private final static String SUBJECT = String.format("[%tF] 주간 공고 추천", LocalDate.now());

    public Mail getMatchedProjectAndConvertMail(
            SimpleMemberTechStackInfoDto member,
            LocalDate startDate,
            LocalDate endDate
    ) {

        List<MemberRecommendationProjectDto> projects =
                projectRepository.findAllByTechStackIdsAndDeadline(
                        member.getTechStackIds(),
                        startDate,
                        endDate);

        return convertMail(member, projects);
    }

    private Mail convertMail(SimpleMemberTechStackInfoDto member,
                             List<MemberRecommendationProjectDto> projects) {
        Context context = new Context();
        context.setVariable("projects", projects);

        String contentTemplate = template.process(TEMPLATE_URL, context);

        return Mail.builder()
                .email(member.getEmail())
                .content(contentTemplate)
                .subject(SUBJECT)
                .build();
    }
}
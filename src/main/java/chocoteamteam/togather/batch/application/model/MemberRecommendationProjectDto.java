package chocoteamteam.togather.batch.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRecommendationProjectDto {
    private Long id;
    private String subject;
    private LocalDate deadline;
    private List<String> techStackNames;
}

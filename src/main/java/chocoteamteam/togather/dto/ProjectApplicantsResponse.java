package chocoteamteam.togather.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProjectApplicantsResponse {

    private Long projectId;
    private List<ApplicantDto> applicantDtos;

}

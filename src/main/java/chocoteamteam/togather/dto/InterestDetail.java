package chocoteamteam.togather.dto;

import chocoteamteam.togather.type.ProjectStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InterestDetail {

    private Long projectId;
    private String title;
    private String writer;
    private ProjectStatus status;
    private LocalDate deadline;

}

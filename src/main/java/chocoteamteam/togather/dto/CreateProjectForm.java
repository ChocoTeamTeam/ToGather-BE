package chocoteamteam.togather.dto;

import chocoteamteam.togather.type.ProjectStatus;
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
public class CreateProjectForm {
    private String title;
    private String content;
    private Integer personnel;
    private ProjectStatus status;
    private String location;
    private LocalDate deadline;
    private List<Long> techStackIds;
}
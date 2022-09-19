package chocoteamteam.togather.dto;

import chocoteamteam.togather.type.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectForm {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Integer personnel;
    @NotNull
    private ProjectStatus status;
    @NotNull
    private Boolean offline;
    private String location;
    @NotNull
    private LocalDate deadline;
    @NotNull
    @Size(min = 1)
    private List<Long> techStackIds;
}
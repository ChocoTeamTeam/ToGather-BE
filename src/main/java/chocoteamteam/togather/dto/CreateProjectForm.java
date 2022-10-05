package chocoteamteam.togather.dto;

import chocoteamteam.togather.entity.Location;
import chocoteamteam.togather.type.ProjectStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectForm {

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

    private String address;
    private Double latitude;
    private Double longitude;

    @NotNull
    private LocalDate deadline;
    @NotNull
    @Size(min = 1)
    private List<Long> techStackIds;

    public Location getLocation() {
        return Location.builder()
                .address(address)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
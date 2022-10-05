package chocoteamteam.togather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDistance {
    @NotNull
    private Integer distance;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
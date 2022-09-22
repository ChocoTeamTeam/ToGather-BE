package chocoteamteam.togather.dto;

import chocoteamteam.togather.type.TechCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTechStackForm {

    @NotBlank
    private String name;
    @NotNull
    private TechCategory category;
    @NotBlank
    private String image;
}

package chocoteamteam.togather.dto;

import chocoteamteam.togather.type.TechCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTechStackForm {
    private Long id;
    private String name;
    private TechCategory type;
    private String image;
}

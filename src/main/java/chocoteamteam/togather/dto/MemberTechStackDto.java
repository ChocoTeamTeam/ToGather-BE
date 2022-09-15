package chocoteamteam.togather.dto;

import chocoteamteam.togather.type.TechCategory;
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
public class MemberTechStackDto {

    private Long id;
    private String name;
    private String image;
    private TechCategory techCategory;
    private MemberDto memberDto;
    private TechStackDto techStackDto;

}

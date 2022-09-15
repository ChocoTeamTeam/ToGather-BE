package chocoteamteam.togather.dto;

import chocoteamteam.togather.type.TechCategory;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TechStackDto {

    private Long id;
    private String name;
    private TechCategory category;
    private String image;

}

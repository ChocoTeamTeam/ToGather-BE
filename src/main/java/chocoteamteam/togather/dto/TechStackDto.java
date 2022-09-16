package chocoteamteam.togather.dto;

import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.type.TechCategory;
import lombok.*;

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

    public static TechStackDto from(TechStack techStack) {
        return TechStackDto.builder()
                .id(techStack.getId())
                .name(techStack.getName())
                .category(techStack.getCategory())
                .image(techStack.getImage())
                .build();
    }
}

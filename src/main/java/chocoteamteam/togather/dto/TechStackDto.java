package chocoteamteam.togather.dto;

import chocoteamteam.togather.dto.queryDslSimpleDto.MemberTechStackInfoDto;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.type.TechCategory;
import java.util.List;
import java.util.stream.Collectors;
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

    public static TechStackDto from(TechStack techStack) {
        return TechStackDto.builder()
            .id(techStack.getId())
            .name(techStack.getName())
            .category(techStack.getCategory())
            .image(techStack.getImage())
            .build();
    }

    public static List<TechStackDto> fromMemberTechStackInfoDtos(
        List<MemberTechStackInfoDto> memberTechStackInfoDtos) {
        return memberTechStackInfoDtos.stream()
            .map(memberTechStackInfoDto -> TechStackDto.builder()
                .id(memberTechStackInfoDto.getTechId())
                .name(memberTechStackInfoDto.getTechName())
                .image(memberTechStackInfoDto.getTechImage())
                .build()
            ).collect(Collectors.toList());
    }
}

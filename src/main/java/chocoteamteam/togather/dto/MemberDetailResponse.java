package chocoteamteam.togather.dto;

import chocoteamteam.togather.dto.queryDslSimpleDto.MemberTechStackInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDetailResponse {

    private Long id;
    private String nickname;
    private String profileImage;
    private List<TechStackDto> techStackDtos;

    public static MemberDetailResponse to(List<MemberTechStackInfoDto> memberTechStackInfoDtos, List<TechStackDto> techStackDtos) {
        return MemberDetailResponse.builder()
            .id(memberTechStackInfoDtos.get(0).getId())
            .nickname(memberTechStackInfoDtos.get(0).getNickname())
            .profileImage(memberTechStackInfoDtos.get(0).getProfileImage())
            .techStackDtos(techStackDtos)
            .build();
    }

}

package chocoteamteam.togather.dto;

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
    private String email;
    private String nickname;
    private String profileImage;
    private List<TechStackDto> techStackDtos;

}

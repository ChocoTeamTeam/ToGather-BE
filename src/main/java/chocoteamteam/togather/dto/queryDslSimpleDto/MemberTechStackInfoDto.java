package chocoteamteam.togather.dto.queryDslSimpleDto;


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
public class MemberTechStackInfoDto {

    private Long id;
    private String nickname;
    private String profileImage;
    private Long techId;
    private String techName;
    private String techImage;

}

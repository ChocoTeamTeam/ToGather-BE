package chocoteamteam.togather.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpServiceDto {

    private String signUpToken;
    private String nickname;
    private String profileImage;
    private List<TechStackDto> techStackDtoList;

}

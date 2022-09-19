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
public class LoginResponse {

    private Long id;
    private String profileImage;
    private List<TechStackDto> techStackDtos;
    private String signUpToken;
    private String accessToken;
    private String refreshToken;
    private String message;
    private boolean loginResult;


}

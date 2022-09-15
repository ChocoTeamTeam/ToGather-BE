package chocoteamteam.togather.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class SignUp {

    @Setter
    @Getter
    @Builder
    public static class Request {

        @NotEmpty
        private String nickname;
        @NotEmpty
        private String profileImage;
        @NotEmpty
        private List<TechStackDto> techs;

    }

    @Setter
    @Getter
    @Builder
    public static class Response {

        private String accessToken;
        private String refreshToken;

    }

}

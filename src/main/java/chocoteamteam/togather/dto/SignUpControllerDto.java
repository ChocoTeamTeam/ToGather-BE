package chocoteamteam.togather.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class SignUpControllerDto {

    @Setter
    @Getter
    @Builder
    public static class Request {

        @NotEmpty
        private String nickname;
        @NotEmpty
        private String profileImage;
        @NotEmpty
        private List<Long> techStackDtos;

    }

    @Setter
    @Getter
    @Builder
    public static class Response {

        private Long id;
        private String profileImage;
        private List<TechStackDto> techStackDtos;
        private String accessToken;
        private String refreshToken;

    }

}

package chocoteamteam.togather.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class SignUpControllerDto {

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @NotEmpty
        private String nickname;
        @NotEmpty
        private String profileImage;
        private List<TechStackDto> techStackDtos;

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

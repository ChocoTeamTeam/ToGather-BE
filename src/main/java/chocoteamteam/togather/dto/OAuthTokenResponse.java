package chocoteamteam.togather.dto;

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
public class OAuthTokenResponse {

    private String id_token;
    private String token_type;
    private String access_token;
    private String refresh_token;

}

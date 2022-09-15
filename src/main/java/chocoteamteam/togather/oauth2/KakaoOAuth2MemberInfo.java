package chocoteamteam.togather.oauth2;

import java.util.Map;
import java.util.Optional;

public class KakaoOAuth2MemberInfo extends OAuth2MemberInfo {

    public KakaoOAuth2MemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public String getId() {
        return attributes.get("id").toString();
    }

    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return (String) properties.get("nickname");
    }

    public Optional<String> getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return Optional.of((String) kakaoAccount.get("email"));
    }
}

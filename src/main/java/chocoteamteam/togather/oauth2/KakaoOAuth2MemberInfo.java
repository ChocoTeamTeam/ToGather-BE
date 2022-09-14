package chocoteamteam.togather.oauth2;

import java.util.Map;

public class KakaoOAuth2MemberInfo extends OAuth2MemberInfo {

    public KakaoOAuth2MemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public String getId() {
        return attributes.get("id").toString();
    }

    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return properties == null ? null : (String) properties.get("nickname");
    }

    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return kakaoAccount == null ? null : (String) kakaoAccount.get("email");
    }
}

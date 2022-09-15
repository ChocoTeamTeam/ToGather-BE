package chocoteamteam.togather.oauth2;

import java.util.Map;
import java.util.Optional;

public class NaverOAuth2MemberInfo extends OAuth2MemberInfo {

    public NaverOAuth2MemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return (String) response.get("id");
    }

    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return (String) response.get("name");
    }

    public Optional<String> getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return Optional.of((String) response.get("email"));
    }
}

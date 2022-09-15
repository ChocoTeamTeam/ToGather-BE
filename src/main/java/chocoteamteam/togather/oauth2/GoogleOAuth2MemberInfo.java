package chocoteamteam.togather.oauth2;

import java.util.Map;
import java.util.Optional;

public class GoogleOAuth2MemberInfo extends OAuth2MemberInfo {

    public GoogleOAuth2MemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public Optional<String> getEmail() {
        return Optional.of((String)attributes.get("email"));
    }
}

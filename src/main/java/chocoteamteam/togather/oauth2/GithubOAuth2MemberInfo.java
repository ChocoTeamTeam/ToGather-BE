package chocoteamteam.togather.oauth2;

import java.util.Map;
import java.util.Optional;

public class GithubOAuth2MemberInfo extends OAuth2MemberInfo {

    public GithubOAuth2MemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public String getId() {
        return (String) attributes.get("id");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable((String) attributes.get("email"));
    }
}

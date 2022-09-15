package chocoteamteam.togather.oauth2;

import java.util.Map;
import java.util.Optional;

public abstract class OAuth2MemberInfo {

    protected Map<String, Object> attributes;

    public OAuth2MemberInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract Optional<String> getEmail();

}

package chocoteamteam.togather.oauth2;

import java.util.Map;

public class NaverOAuth2MemberInfo extends OAuth2MemberInfo {

    public NaverOAuth2MemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return response == null ? null : (String)response.get("id");
    }

    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return response == null ? null : (String)response.get("name");
    }

    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return response == null ? null : (String)response.get("email");
    }
}

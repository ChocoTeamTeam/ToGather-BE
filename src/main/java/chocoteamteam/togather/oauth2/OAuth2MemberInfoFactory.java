package chocoteamteam.togather.oauth2;

import chocoteamteam.togather.exception.CustomOAuthException;
import chocoteamteam.togather.exception.ErrorCode;
import java.util.Map;

public class OAuth2MemberInfoFactory {
    public OAuth2MemberInfoFactory() {
    }

    public static OAuth2MemberInfo getOAuth2MemberInfo(String providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case "GOOGLE":
                return new GoogleOAuth2MemberInfo(attributes);
            case "NAVER":
                return new NaverOAuth2MemberInfo(attributes);
            case "KAKAO":
                return new KakaoOAuth2MemberInfo(attributes);
            case "GITHUB":
                return new GithubOAuth2MemberInfo(attributes);
            default:
                throw new CustomOAuthException(ErrorCode.ABNORMAL_ACCESS);
        }
    }
}

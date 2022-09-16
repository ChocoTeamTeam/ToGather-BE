package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.LoginResponse;
import chocoteamteam.togather.dto.OAuthTokenResponse;
import chocoteamteam.togather.dto.SignUpControllerDto;
import chocoteamteam.togather.dto.SignUpControllerDto.Response;
import chocoteamteam.togather.dto.SignUpServiceDto;
import chocoteamteam.togather.dto.SignUpTokenMemberInfo;
import chocoteamteam.togather.dto.TokenMemberInfo;
import chocoteamteam.togather.dto.Tokens;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.MemberTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.CustomOAuthException;
import chocoteamteam.togather.exception.TechStackException;
import chocoteamteam.togather.oauth2.OAuth2MemberInfo;
import chocoteamteam.togather.oauth2.OAuth2MemberInfoFactory;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.LoginStatus;
import chocoteamteam.togather.type.MemberStatus;
import chocoteamteam.togather.type.ProviderType;
import chocoteamteam.togather.type.Role;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final TechStackRepository techStackRepository;
    private final MemberTechStackRepository memberTechStackRepository;
    private final InMemoryClientRegistrationRepository inMemoryClientRegistrationRepository;
    private final JwtService jwtService;

    public LoginResponse login(String code, String providerType) {

        ClientRegistration provider = inMemoryClientRegistrationRepository.findByRegistrationId(
            providerType);

        OAuthTokenResponse oAuthToken = getOAuth2Token(code, provider);

        Map<String, Object> attributes = getAttributes(oAuthToken, provider);

        OAuth2MemberInfo oAuth2MemberInfo = OAuth2MemberInfoFactory
            .getOAuth2MemberInfo(providerType.toUpperCase(), attributes);

        String email = oAuth2MemberInfo.getEmail()
            .orElseThrow(() -> new CustomOAuthException("이메일을 찾을 수 없습니다."));

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty()) {
            String signUpToken = jwtService.issueSignUpToken(email, providerType.toUpperCase());
            return LoginResponse.builder()
                .signUpToken(signUpToken)
                .message(LoginStatus.NEW.getMessage())
                .loginResult(LoginStatus.NEW.isFoundMember())
                .build();
        }

        Member member = optionalMember.get();

        if (!member.getProviderType().toString().equals(providerType.toUpperCase())) {
            throw new CustomOAuthException(member.getProviderType() + " 아이디로 로그인하시길 바랍니다.");
        }

        Tokens tokens = getTokens(member);
        return LoginResponse.builder()
            .accessToken(tokens.getAccessToken())
            .refreshToken(tokens.getRefreshToken())
            .message(LoginStatus.EXIST.getMessage())
            .loginResult(LoginStatus.EXIST.isFoundMember())
            .build();
    }

    @Transactional
    public SignUpControllerDto.Response signUp(SignUpServiceDto signUpServiceDto) {

        SignUpTokenMemberInfo signUpTokenMemberInfo =
            jwtService.parseSignUpToken(signUpServiceDto.getSignUpToken());

        if (memberRepository.existsByNickname(signUpServiceDto.getNickname())) {
            throw new CustomOAuthException("이미 존재하는 닉네임입니다.");
        }

        Member member = memberRepository.save(
            Member.builder()
                .email(signUpTokenMemberInfo.getEmail())
                .nickname(signUpServiceDto.getNickname())
                .profileImage(signUpServiceDto.getProfileImage())
                .status(MemberStatus.PERMITTED)
                .role(Role.ROLE_USER)
                .providerType(ProviderType.valueOf(signUpTokenMemberInfo.getProvider()))
                .build()
        );

        List<TechStack> techStacks = getTechsById(signUpServiceDto.getTechStackDtoList());

        registerTechMembers(member, techStacks);

        Tokens tokens = getTokens(member);

        return Response.builder()
            .accessToken(tokens.getAccessToken())
            .refreshToken(tokens.getRefreshToken())
            .build();
    }

    private Tokens getTokens(Member member) {
        return jwtService.issueTokens(
            TokenMemberInfo.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .role(member.getRole().toString())
                .status(member.getStatus().toString())
                .build());
    }

    private List<TechStack> getTechsById(List<Long> techStackDtos) {
        List<TechStack> techStacks = techStackRepository.findAllById(techStackDtos);
        if (techStacks.isEmpty()) {
            throw new TechStackException("기술이 존재하지 않습니다.");
        }
        return techStacks;
    }

    private void registerTechMembers(Member member, List<TechStack> techStacks) {
        for (TechStack techStack : techStacks) {
            memberTechStackRepository.save(MemberTechStack.builder()
                .member(member)
                .techStack(techStack)
                .build());
        }
    }

    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_id", provider.getClientId());
        formData.add("client_secret", provider.getClientSecret());
        return formData;
    }

    private OAuthTokenResponse getOAuth2Token(String code, ClientRegistration provider) {
        return WebClient.create()
            .post()
            .uri(provider.getProviderDetails().getTokenUri())
            .headers(header -> {
                header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            })
            .bodyValue(tokenRequest(code, provider))
            .retrieve()
            .bodyToMono(OAuthTokenResponse.class)
            .block();
    }

    private Map<String, Object> getAttributes(OAuthTokenResponse oAuthToken,
        ClientRegistration provider) {
        return WebClient.create()
            .get()
            .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
            .headers(httpHeader -> httpHeader.setBearerAuth(oAuthToken.getAccess_token()))
            .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block();
    }

}
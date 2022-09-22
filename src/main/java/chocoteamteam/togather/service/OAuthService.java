package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.*;
import chocoteamteam.togather.dto.SignUpControllerDto.Response;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.MemberTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.CustomOAuthException;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.TechStackException;
import chocoteamteam.togather.oauth2.OAuth2MemberInfo;
import chocoteamteam.togather.oauth2.OAuth2MemberInfoFactory;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackRepository;
import chocoteamteam.togather.repository.RefreshTokenRepository;
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
import java.util.stream.Collectors;
import lombok.NonNull;
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

import static chocoteamteam.togather.exception.ErrorCode.NOT_FOUND_TECH_STACK;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final TechStackRepository techStackRepository;
    private final MemberTechStackRepository memberTechStackRepository;
    private final InMemoryClientRegistrationRepository inMemoryClientRegistrationRepository;
    private final JwtService jwtService;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponse login(String code, String providerType) {

        ClientRegistration provider = inMemoryClientRegistrationRepository.findByRegistrationId(
            providerType);

        OAuthTokenResponse oAuthToken = getOAuth2Token(code, provider);

        Map<String, Object> attributes = getAttributes(oAuthToken, provider);

        OAuth2MemberInfo oAuth2MemberInfo = OAuth2MemberInfoFactory
            .getOAuth2MemberInfo(providerType.toUpperCase(), attributes);

        String email = oAuth2MemberInfo.getEmail()
            .orElseThrow(() -> new CustomOAuthException(ErrorCode.NOT_FOUND_EMAIL));

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
            throw new CustomOAuthException(ErrorCode.MISS_MATCH_PROVIDER);
        }

        if (member.getStatus() != MemberStatus.PERMITTED) {
            throw new CustomOAuthException(ErrorCode.MEMBER_STATUS_WITHDRAWAL);
        }

        Tokens tokens = getTokens(member);

        return LoginResponse.builder()
            .id(member.getId())
            .nickname(member.getNickname())
            .profileImage(member.getProfileImage())
            .techStackDtos(getTechStackDtosFromMember(member))
            .accessToken(tokens.getAccessToken())
            .refreshToken(tokens.getRefreshToken())
            .message(LoginStatus.EXIST.getMessage())
            .loginResult(LoginStatus.EXIST.isFoundMember())
            .build();
    }

    private List<TechStackDto> getTechStackDtosFromMember(Member member) {
        List<Long> techStackIds = member.getMemberTechStacks().stream()
            .map(memberTechStack -> memberTechStack.getTechStack().getId())
            .collect(Collectors.toList());

        return techStackRepository.findAllById(techStackIds).stream()
            .map(TechStackDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public SignUpControllerDto.Response signUp(SignUpServiceDto signUpServiceDto) {

        SignUpTokenMemberInfo signUpTokenMemberInfo =
            jwtService.parseSignUpToken(signUpServiceDto.getSignUpToken());

        if (memberRepository.existsByNickname(signUpServiceDto.getNickname())) {
            throw new CustomOAuthException(ErrorCode.EXIST_TRUE_MEMBER_NICKNAME);
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

        registerMemberTechStack(member, techStacks);

        Tokens tokens = getTokens(member);

        return Response.builder()
            .id(member.getId())
            .profileImage(member.getProfileImage())
            .techStackDtos(techStacks.stream().map(TechStackDto::from).collect(Collectors.toList()))
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
            throw new TechStackException(NOT_FOUND_TECH_STACK);
        }
        return techStacks;
    }

    private void registerMemberTechStack(Member member, List<TechStack> techStacks) {
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

    @Transactional
    public void logout(@NonNull Long memberId) {
        refreshTokenRepository.delete(memberId);
    }

}
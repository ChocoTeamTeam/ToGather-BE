package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import chocoteamteam.togather.dto.SignUp;
import chocoteamteam.togather.dto.SignUpTokenMemberInfo;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.dto.Tokens;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.CustomOAuthException;
import chocoteamteam.togather.exception.TechStackException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.MemberStatus;
import chocoteamteam.togather.type.ProviderType;
import chocoteamteam.togather.type.Role;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class OAuthServiceTest {

    @Mock
    JwtService jwtService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    TechStackRepository techStackRepository;
    @Mock
    MemberTechStackRepository memberTechStackRepository;

    @InjectMocks
    OAuthService oAuthService;

    String signUpToken = "signToken";
    String nickname = "name";
    String profileImage = "image";
    String accessToken = "12345";
    String refreshToken = "54321";
    String email = "test@test.com";
    String provider = "GOOGLE";
    List<TechStackDto> techStackDtos = new ArrayList(
        List.of(TechStackDto.builder().id(1L).build()));

    @DisplayName("회원가입 성공")
    @Test
    void signUp_success() {
        // given
        given(memberRepository.save(any())).willReturn(Member.builder()
            .id(1L)
            .email(email)
            .nickname("test")
            .profileImage(profileImage)
            .status(MemberStatus.PERMITTED)
            .role(Role.ROLE_USER)
            .providerType(ProviderType.GOOGLE)
            .build()
        );
        given(techStackRepository.findAllById(any())).willReturn(
            List.of(TechStack.builder().build()));
        given(jwtService.parseSignUpToken(any())).willReturn(SignUpTokenMemberInfo.builder()
            .email(email)
            .provider(provider)
            .build());
        given(jwtService.issueTokens(any())).willReturn(Tokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build());
        given(memberTechStackRepository.save(any())).willReturn(
            chocoteamteam.togather.entity.MemberTechStack.builder().build());

        // when
        SignUp.Response response = oAuthService.signUp(signUpToken, nickname,
            profileImage, techStackDtos);

        // then
        Assertions.assertThat(response.getAccessToken()).isEqualTo(accessToken);
        Assertions.assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
    }

    @DisplayName("회원 가입 실패 - 기술이 존재하지 않음")
    @Test
    void signUp_fail_not_found_techStack() {
        // given
        given(jwtService.parseSignUpToken(any())).willReturn(SignUpTokenMemberInfo
            .builder()
            .email("test")
            .provider("GOOGLE")
            .build());
        given(memberRepository.save(any())).willReturn(Member.builder().build());

        // when

        // then
        assertThatThrownBy(
            () -> oAuthService.signUp(signUpToken, nickname, profileImage, techStackDtos))
            .isInstanceOf(TechStackException.class)
            .hasMessage("기술이 존재하지 않습니다.");
    }

    @DisplayName("회원 가입 실패 - 이미 존재하는 닉네임")
    @Test
    void signUp_fail_exits_true_nickname() {
        // given
        given(memberRepository.existsByNickname(nickname)).willReturn(true);
        given(jwtService.parseSignUpToken(any())).willReturn(
            SignUpTokenMemberInfo
                .builder()
                .email(email)
                .provider(provider)
                .build()
        );

        // when

        // then
        assertThatThrownBy(
            () -> oAuthService.signUp(signUpToken, nickname, profileImage, techStackDtos))
            .isInstanceOf(CustomOAuthException.class)
            .hasMessage("이미 존재하는 닉네임입니다.");
    }

}

package chocoteamteam.togather.service;

import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_ID;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_NICKNAME;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_PROVIDER;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_ROLES;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import chocoteamteam.togather.component.jwt.JwtIssuer;
import chocoteamteam.togather.component.jwt.JwtParser;
import chocoteamteam.togather.component.jwt.JwtUtils;
import chocoteamteam.togather.dto.SignUpTokenMemberInfo;
import chocoteamteam.togather.dto.TokenMemberInfo;
import chocoteamteam.togather.dto.Tokens;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.TokenException;
import chocoteamteam.togather.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

	@Mock
	JwtUtils jwtUtils;
	@Mock
	JwtIssuer jwtIssuer;
	@Mock
	JwtParser jwtParser;
	@Mock
	RefreshTokenRepository refreshTokenRepository;
	@Spy
	@InjectMocks
	JwtService jwtService;

	static Claims claims;

	@BeforeAll
	static void beforeAll() {
		claims = Jwts.claims();
		claims.put(KEY_ID, 10L);
		claims.put(KEY_ROLES, "ROLE_USER");
		claims.put(KEY_NICKNAME, "test");
		claims.put(KEY_STATUS, "WAIT");
	}


	@DisplayName("토큰 발급 성공")
	@Test
	void issueTokens_success() {
		//given
		TokenMemberInfo info = TokenMemberInfo.builder()
			.id(1L)
			.nickname("test")
			.status("PERMITTED")
			.role("ROLE_USER")
			.build();

		given(jwtIssuer.issueToken(any(), any()))
			.willReturn("accessToken", "refreshToken");

		given(jwtUtils.getAccessTokenExpiredMin())
			.willReturn(120);

		//when
		Tokens tokens = jwtService.issueTokens(info);

		//then
		assertThat(tokens.getAccessToken()).isEqualTo("accessToken");
		assertThat(tokens.getRefreshToken()).isEqualTo("refreshToken");
		assertThat(tokens.getAccessExpires()).isEqualTo(120);
		assertThat(tokens.getGrantType()).isEqualTo("Bearer ");
	}

	@DisplayName("토큰 발급 실패 - TokenMemberInfo가 null")
	@Test
	void issueTokens_fail_tokenMemberInfoIsNull() {
		//given
		//when
		//then
		assertThatThrownBy(() -> jwtService.issueTokens(null))
			.isInstanceOf(NullPointerException.class);
	}

	@DisplayName("토큰 재발급 성공")
	@Test
	void refreshTokens() {
		//given
		given(jwtParser.parseToken(any(), any()))
			.willReturn(claims);

		given(refreshTokenRepository.find(anyLong()))
			.willReturn(Optional.of("testToken"));

		Tokens tokens = Tokens.builder()
			.accessToken("access")
			.refreshToken("refresh")
			.grantType("BEARER ")
			.accessExpires(10)
			.build();

		doReturn(tokens).when(jwtService).issueTokens(any());

		//when
		Tokens expectedTokens = jwtService.refreshTokens("testToken");

		//then
		assertThat(expectedTokens.getAccessToken()).isEqualTo(tokens.getAccessToken());
		assertThat(expectedTokens.getRefreshToken()).isEqualTo(tokens.getRefreshToken());
		assertThat(expectedTokens.getGrantType()).isEqualTo(tokens.getGrantType());
		assertThat(expectedTokens.getAccessExpires()).isEqualTo(tokens.getAccessExpires());

	}

	@DisplayName("토큰 갱신 실패 - 파라미터로 들어오는 Refresh Token이 null")
	@Test
	void refreshToken_fail_refreshTokenIsNull(){
	    //given
	    //when
	    //then
		assertThatThrownBy(()->jwtService.refreshTokens(null))
			.isInstanceOf(NullPointerException.class);
	}

	@DisplayName("토큰 갱신 실패 - Refresh Token이 Redis에 저장되어 있지 않은 경우")
	@Test
	void refreshToken_fail_NotExistsRefreshToken(){
	    //given
		given(jwtParser.parseToken(any(), any()))
			.willReturn(claims);

		given(refreshTokenRepository.find(anyLong()))
			.willReturn(Optional.empty());

		//when
	    //then
		assertThatThrownBy(() -> jwtService.refreshTokens("test"))
			.isInstanceOf(TokenException.class)
			.hasMessage(ErrorCode.INVALID_TOKEN.getErrorMessage());
	}

	@DisplayName("토큰 갱신 실패 - 저장된 Refresh Token과 파라미터 Refresh Token이 다름")
	@Test
	void refreshToken_fail_RefreshTokenNotMatch(){
	    //given
		given(jwtParser.parseToken(any(), any()))
			.willReturn(claims);

		given(refreshTokenRepository.find(anyLong()))
			.willReturn(Optional.of("testToken"));

		//when
	    //then
		assertThatThrownBy(() -> jwtService.refreshTokens("test"))
			.isInstanceOf(TokenException.class)
			.hasMessage(ErrorCode.INVALID_TOKEN.getErrorMessage());
	}

	@DisplayName("Access Token 파싱 성공")
	@Test
	void parseAccessToken_success() {
		//given
		given(jwtParser.parseToken(any(), any()))
			.willReturn(claims);

		//when
		TokenMemberInfo info = jwtService.parseAccessToken("test");

		//then
		assertThat(info.getId()).isEqualTo(claims.get(KEY_ID, Long.class));
		assertThat(info.getNickname()).isEqualTo(claims.get(KEY_NICKNAME, String.class));
		assertThat(info.getStatus()).isEqualTo(claims.get(KEY_STATUS, String.class));
		assertThat(info.getRole()).isEqualTo(claims.get(KEY_ROLES, String.class));
	}

	@DisplayName("Access Token 파싱 실패 - access Token이 null인 경우")
	@Test
	void parseAccessToken_fail_accessTokenIsNull() {
		//given
		//when
		//then
		assertThatThrownBy(() -> jwtService.parseAccessToken(null))
			.isInstanceOf(NullPointerException.class);
	}

	@DisplayName("SignUp Token 발급 성공")
	@Test
	void issueSignUpToken_success() {
		//given
		given(jwtIssuer.issueToken(any(), any()))
			.willReturn("signUpToken");

		//when
		String token = jwtService.issueSignUpToken("test@test.com", "kakao");

		//then
		assertThat(token).isEqualTo("signUpToken");
	}

	@DisplayName("SignUp Token 발급 실패 - 매개변수들이 null인 경우")
	@Test
	void issueSignUpToken_fail_parameterIsNull() {
		//given
		//when
		//then
		assertThatThrownBy(() -> jwtService.issueSignUpToken(null,"kakao"))
			.isInstanceOf(NullPointerException.class);
		assertThatThrownBy(() -> jwtService.issueSignUpToken(null,null))
			.isInstanceOf(NullPointerException.class);
		assertThatThrownBy(() -> jwtService.issueSignUpToken("test",null))
			.isInstanceOf(NullPointerException.class);
	}

	@DisplayName("SignUp Token 파싱 성공")
	@Test
	void parseSignUpToken_success(){
	    //given
		Claims claims = Jwts.claims();

		claims.put(KEY_ID, "test");
		claims.put(KEY_PROVIDER, "kakao");

		given(jwtParser.parseToken(any(), any()))
			.willReturn(claims);

	    //when
		SignUpTokenMemberInfo info = jwtService.parseSignUpToken("signUpToken");

		//then
		assertThat(info.getEmail()).isEqualTo("test");
		assertThat(info.getProvider()).isEqualTo("kakao");

	}

	@DisplayName("SignUp Token 파싱 실패 - 매개변수들이 null인 경우")
	@Test
	void parseSignUpToken_fail_parameterIsNull() {
		//given
		//when
		//then
		assertThatThrownBy(() -> jwtService.parseSignUpToken(null))
			.isInstanceOf(NullPointerException.class);
	}

	@DisplayName("SignUp Token 파싱 실패 - 회원가입 토큰이 아닌 경우")
	@Test
	void parseSignUpToken_fail_InvalidSignUpToken() {
		//given
		given(jwtParser.parseToken(any(), any()))
			.willReturn(claims);

		//when
		//then
		assertThatThrownBy(() -> jwtService.parseSignUpToken("singUpToken"))
			.isInstanceOf(TokenException.class)
			.hasMessage(ErrorCode.INVALID_TOKEN.getErrorMessage());
	}


}
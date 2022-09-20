package chocoteamteam.togather.component.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtParserTest {

	JwtIssuer jwtIssuer = new JwtIssuer();

	@InjectMocks
	JwtParser jwtParser;

	Claims claims;
	Key secretKey;
	String token;


	@BeforeEach
	void beforeEach() {
		Date date = new Date();

		claims = Jwts.claims().setSubject("test").setIssuedAt(date)
			.setExpiration(new Date(date.getTime() + 6000));

		secretKey = Keys.hmacShaKeyFor(Base64.getEncoder()
			.encodeToString("secretKeysecretKeysecretKeysecretKeysecretKey".getBytes()).getBytes());

		token = jwtIssuer.issueToken(claims, secretKey);

	}

	@DisplayName("토큰 파싱 성공")
	@Test
	void parseToken_success() {
		//given
		//when
		Claims parsedClaims = jwtParser.parseToken(token, secretKey);

		//then
		assertThat(parsedClaims.getSubject()).isEqualTo(claims.getSubject());
	}


	@DisplayName("토큰 파싱 실패 - JWT 유효시간이 만료")
	@Test
	void parseToken_fail_expiredToken() {
		//given
		Date now = new Date();

		Claims claims = Jwts.claims().setSubject("test").setIssuedAt(now)
			.setExpiration(now);

		String token = jwtIssuer.issueToken(claims, secretKey);

		//when
		//then
		assertThatThrownBy(() -> jwtParser.parseToken(token, secretKey))
			.isInstanceOf(TokenException.class)
			.hasMessage(ErrorCode.INVALID_TOKEN.getErrorMessage());
	}

	@DisplayName("토큰 파싱 실패 - JWT 시그니쳐가 잘못되었을 경우")
	@Test
	void parseToken_fail_invalidSignature() {
		//given
		String token = jwtIssuer.issueToken(claims,
			Keys.hmacShaKeyFor(Base64.getEncoder()
				.encodeToString("invalidTestKeyinvalidTestKeyinvalidTestKey".getBytes())
				.getBytes()));

		//when
		//then
		assertThatThrownBy(() -> jwtParser.parseToken(token, secretKey))
			.isInstanceOf(TokenException.class)
			.hasMessage(ErrorCode.INVALID_TOKEN.getErrorMessage());
	}

	@DisplayName("토큰 파싱 실패 - JWT 형식이 잘못된 경우")
	@Test
	void parseToken_fail_malformedJwt() {
		//given
		//when
		//then
		assertThatThrownBy(() -> jwtParser.parseToken(token + ".malformed", secretKey))
			.isInstanceOf(TokenException.class)
			.hasMessage(ErrorCode.INVALID_TOKEN.getErrorMessage());
	}

	@DisplayName("토큰 파싱 실패 - JWT 값 자체가 유효하지않을 경우")
	@Test
	void parseToken_fail_IllegalArgument() {
		//given
		//when
		//then
		assertThatThrownBy(() -> jwtParser.parseToken(" ", secretKey))
			.isInstanceOf(TokenException.class)
			.hasMessage(ErrorCode.INVALID_TOKEN.getErrorMessage());
	}
}
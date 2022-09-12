package chocoteamteam.togather.component.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtIssuerTest {

	@InjectMocks
	JwtIssuer jwtIssuer;
	static Claims claims;
	static Key secretKey;

	@BeforeAll
	static void beforeAll() {
		Date date = new Date();

		claims = Jwts.claims().setSubject("test").setIssuedAt(date)
			.setExpiration(date);

		secretKey = Keys.hmacShaKeyFor(Base64.getEncoder()
			.encodeToString("secretKeysecretKeysecretKeysecretKeysecretKey".getBytes()).getBytes());
	}

	@DisplayName("Token 발급 성공")
	@Test
	void issueToken_success() {
		//given

		//when
		String token = jwtIssuer.issueToken(claims,secretKey);
		String[] split = token.split("\\.");

		//then
		assertThat(token).isNotNull();
		assertThat(split.length).isEqualTo(3);
	}


	@DisplayName("토큰 발급 실패 - Claims이 null일 경우")
	@Test
	void issueToken_fail_claimsIsNull() {
		//given
		//when
		//then
		assertThatThrownBy(() -> jwtIssuer.issueToken(null,secretKey))
			.isInstanceOf(NullPointerException.class);
	}

	@DisplayName("토큰 발급 실패 - secretKey가 null일 경우")
	@Test
	void issueToken_fail_secretKeyIsNull() {
		//given
		//when
		//then
		assertThatThrownBy(() -> jwtIssuer.issueToken(claims,null))
			.isInstanceOf(NullPointerException.class);
	}


}


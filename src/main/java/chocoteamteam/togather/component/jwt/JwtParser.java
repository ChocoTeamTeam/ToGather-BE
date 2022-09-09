package chocoteamteam.togather.component.jwt;

import chocoteamteam.togather.exception.JwtParseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtParser {

	public Claims parseToken(String token, byte[] secretKey) {
		Claims claims;

		try {
			claims = Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(secretKey))
				.build()
				.parseClaimsJws(token).getBody();
		} catch (SignatureException e) {
			throw new JwtParseException("JWT의 시그니쳐가 유효하지 않습니다.", e);
		} catch (MalformedJwtException e) {
			throw new JwtParseException("JWT의 형식이 잘못되었습니다.", e);
		} catch (ExpiredJwtException e) {
			throw new JwtParseException("JWT의 유효시간이 만료되었습니다.", e);
		} catch (UnsupportedJwtException e) {
			throw new JwtParseException("지원하지 않는 JWT 입니다.", e);
		} catch (IllegalArgumentException e) {
			throw new JwtParseException("유효하지 않은 JWT 입니다.", e);
		}

		return claims;
	}
}

package chocoteamteam.togather.component.jwt;

import chocoteamteam.togather.exception.JwtParseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtParser {

	public Claims parseToken(String token, Key secretKey) {
		Claims claims;

		try {
			claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			throw new JwtParseException("JWT의 유효시간이 만료되었습니다.", e);
		} catch (SignatureException | MalformedJwtException | IllegalArgumentException |
				 UnsupportedJwtException e) {
			throw new JwtParseException("유효하지 않은 JWT 입니다.", e);
		}

		return claims;
	}
}

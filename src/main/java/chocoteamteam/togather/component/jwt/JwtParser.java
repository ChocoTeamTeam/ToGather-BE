package chocoteamteam.togather.component.jwt;

import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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
		} catch (Exception e) {
			throw new TokenException(ErrorCode.INVALID_TOKEN, e);
		}

		return claims;
	}
}

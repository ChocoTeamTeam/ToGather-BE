package chocoteamteam.togather.component.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtIssuer {

	public String issueToken(@NonNull Claims claims, @NonNull byte[] secretKey) {
		return Jwts.builder()
			.setClaims(claims)
			.signWith(Keys.hmacShaKeyFor(secretKey))
			.compact();
	}

}

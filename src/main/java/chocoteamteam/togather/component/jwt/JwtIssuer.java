package chocoteamteam.togather.component.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtIssuer {

	public String issueToken(@NonNull Claims claims, @NonNull Key secretKey) {
		return Jwts.builder()
			.setClaims(claims)
			.signWith(secretKey)
			.compact();
	}

}

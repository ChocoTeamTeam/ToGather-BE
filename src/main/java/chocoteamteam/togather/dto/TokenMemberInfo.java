package chocoteamteam.togather.dto;


import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_ID;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_NICKNAME;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_ROLES;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_STATUS;
import static javax.management.timer.Timer.ONE_MINUTE;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TokenMemberInfo {

	private Long id;
	private String nickname;
	private String status;
	private String role;

	public Claims toClaims(int expiresMin) {
		Claims claims = Jwts.claims();

		Date now = new Date();

		claims.put(KEY_ID,this.id);
		claims.put(KEY_NICKNAME,this.nickname);
		claims.put(KEY_STATUS, this.status);
		claims.put(KEY_ROLES,this.role);
		claims.setIssuedAt(now);
		claims.setExpiration(new Date(now.getTime() + expiresMin * ONE_MINUTE));

		return claims;
	}

	public static TokenMemberInfo from(Claims claims) {
		return TokenMemberInfo.builder()
			.id(claims.get(KEY_ID, Long.class))
			.nickname(claims.get(KEY_NICKNAME, String.class))
			.role(claims.get(KEY_ROLES, String.class))
			.status(claims.get(KEY_STATUS, String.class))
			.build();
	}
}

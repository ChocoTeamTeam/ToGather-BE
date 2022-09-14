package chocoteamteam.togather.dto;


import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_ID;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_PROVIDER;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUpTokenMemberInfo {

	 private String email;
	 private String provider;


	public static SignUpTokenMemberInfo from(Claims claims) {
		return SignUpTokenMemberInfo.builder()
			.email(claims.get(KEY_ID, String.class))
			.provider(claims.get(KEY_PROVIDER, String.class))
			.build();
	}

}

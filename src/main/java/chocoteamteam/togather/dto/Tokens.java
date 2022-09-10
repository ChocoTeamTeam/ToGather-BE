package chocoteamteam.togather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Tokens {

	private String accessToken;
	private String refreshToken;
	private int accessExpires;
	private String grantType;

}

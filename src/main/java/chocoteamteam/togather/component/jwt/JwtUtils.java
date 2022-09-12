package chocoteamteam.togather.component.jwt;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
public class JwtUtils {

	public static final String BEARER_PREFIX = "Bearer ";
	public static final String KEY_ID = "id";
	public static final String KEY_NICKNAME = "nickname";
	public static final String KEY_STATUS = "status";
	public static final String KEY_ROLES = "roles";

	@Value("${jwt.secret-key.access}")
	private String accessKey;

	@Value("${jwt.secret-key.refresh}")
	private String refreshKey;

	private Key encodedAccessKey;
	private Key encodedRefreshKey;

	@Value("${jwt.expired-min.access}")
	private int accessTokenExpiredMin;

	@Value("${jwt.expired-min.refresh}")
	private int refreshTokenExpiredMin;

	@PostConstruct
	private void init() {
		encodedAccessKey = Keys.hmacShaKeyFor(
			Base64.getEncoder().encodeToString(accessKey.getBytes()).getBytes());

		encodedRefreshKey = Keys.hmacShaKeyFor(
			Base64.getEncoder().encodeToString(refreshKey.getBytes()).getBytes());
	}

}

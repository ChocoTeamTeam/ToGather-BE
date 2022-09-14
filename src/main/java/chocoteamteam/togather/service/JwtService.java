package chocoteamteam.togather.service;

import static chocoteamteam.togather.component.jwt.JwtUtils.BEARER_PREFIX;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_ID;
import static chocoteamteam.togather.component.jwt.JwtUtils.KEY_PROVIDER;

import chocoteamteam.togather.dto.SignUpTokenMemberInfo;
import chocoteamteam.togather.exception.InvalidSignUpTokenException;
import chocoteamteam.togather.repository.RefreshTokenRepository;
import chocoteamteam.togather.component.jwt.JwtUtils;
import chocoteamteam.togather.component.jwt.JwtIssuer;
import chocoteamteam.togather.component.jwt.JwtParser;
import chocoteamteam.togather.dto.TokenMemberInfo;
import chocoteamteam.togather.dto.Tokens;
import chocoteamteam.togather.exception.InvalidRefreshTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class JwtService {

	private final JwtUtils jwtUtils;
	private final JwtIssuer jwtIssuer;
	private final JwtParser jwtParser;
	private final RefreshTokenRepository refreshTokenRepository;

	// 토큰 발급
	@Transactional
	public Tokens issueTokens(@NonNull TokenMemberInfo info) {

		String accessToken = jwtIssuer.issueToken(
			info.toClaims(jwtUtils.getAccessTokenExpiredMin()), jwtUtils.getEncodedAccessKey());
		String refreshToken = jwtIssuer.issueToken(
			info.toClaims(jwtUtils.getRefreshTokenExpiredMin()), jwtUtils.getEncodedRefreshKey());

		refreshTokenRepository.
			save(info.getId(), refreshToken, jwtUtils.getRefreshTokenExpiredMin());

		return Tokens.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.accessExpires(jwtUtils.getAccessTokenExpiredMin())
			.grantType(BEARER_PREFIX)
			.build();
	}

	// 토큰 재발급
	@Transactional
	public Tokens refreshTokens(@NonNull String refreshToken) {

		Claims claims = jwtParser.parseToken(refreshToken, jwtUtils.getEncodedRefreshKey());

		String savedToken = refreshTokenRepository.find(claims.get(KEY_ID, Long.class))
			.orElseThrow(() -> new InvalidRefreshTokenException("Refresh Token이 유효하지 않습니다."));

		validateRefreshTokens(refreshToken, savedToken);

		return issueTokens(TokenMemberInfo.from(claims));
	}

	private void validateRefreshTokens(String refreshToken, String savedToken) {
		if (!refreshToken.equals(savedToken)) {
			throw new InvalidRefreshTokenException("Refresh Token이 유효하지 않습니다.");
		}
	}

	// 액세스 토큰 파싱
	public TokenMemberInfo parseAccessToken(@NonNull String accessToken) {
		return TokenMemberInfo.from(
			jwtParser.parseToken(accessToken, jwtUtils.getEncodedAccessKey()));
	}

	public String issueSignUpToken(@NonNull String email,@NonNull String provider) {
		Claims claims = Jwts.claims();
		claims.put(KEY_ID, email);
		claims.put(KEY_PROVIDER, provider);

		return jwtIssuer.issueToken(claims, jwtUtils.getEncodedSignupKey());
	}

	public SignUpTokenMemberInfo parseSignUpToken(@NonNull String signUpToken) {
		Claims claims = jwtParser.parseToken(signUpToken, jwtUtils.getEncodedSignupKey());

		if (!StringUtils.hasText(claims.get(KEY_PROVIDER, String.class))) {
			throw new InvalidSignUpTokenException("유효한 회원가입 토큰이 아닙니다.");
		}

		return SignUpTokenMemberInfo.from(claims);
	}

}

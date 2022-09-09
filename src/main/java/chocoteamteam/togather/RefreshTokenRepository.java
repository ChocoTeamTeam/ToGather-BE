package chocoteamteam.togather;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepository {

	private final RedisTemplate<String, String> redisTemplate;

	private static final String KEY_PREFIX = "refreshToken::";

	public void save(long userId, String token, int expiresMin) {
		redisTemplate.opsForValue().set(KEY_PREFIX + userId, token, Duration.ofMinutes(expiresMin));
	}

	public boolean exists(long userId) {
		String token = redisTemplate.opsForValue().get(KEY_PREFIX + userId);

		if (!StringUtils.hasText(token)) {
			return false;
		}

		return true;
	}

	public Optional<String> find(long userId) {
		String token = redisTemplate.opsForValue().get(KEY_PREFIX + userId);

		return Optional.ofNullable(token);
	}

	public void delete(long userId) {
		redisTemplate.delete(KEY_PREFIX + userId);
	}
}

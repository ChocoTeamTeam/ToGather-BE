package chocoteamteam.togather.repository;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Repository
public class FCMRegistrationTokenRepository {

	private final RedisTemplate<String, String> redisTemplate;

	private static final String KEY_PREFIX = "FcmRegistrationToken::";

	public void save(long userId, String token) {
		redisTemplate.opsForValue().set(KEY_PREFIX + userId, token, Duration.ofDays(60));
	}

	public boolean exists(long userId) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + userId));

	}

	public Optional<String> find(long userId) {
		String token = redisTemplate.opsForValue().get(KEY_PREFIX + userId);

		return Optional.ofNullable(token);
	}

	public void delete(long userId) {
		redisTemplate.delete(KEY_PREFIX + userId);
	}
}

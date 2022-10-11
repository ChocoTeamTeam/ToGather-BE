package chocoteamteam.togather.service;

import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.FCMException;
import chocoteamteam.togather.repository.FCMRegistrationTokenRepository;
import com.google.api.core.ApiFuture;
import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FCMService {

	private final FCMRegistrationTokenRepository tokenRepository;

	public void saveRegistrationToken(Long memberId, String registrationToken) {
		log.info("member id = {} , register FCM Token = {}",memberId,registrationToken);
		tokenRepository.save(memberId,registrationToken);
	}

	public void deleteRegistrationToken(Long memberId) {
		log.info("member id = {} , delete FCM Token",memberId);
		tokenRepository.delete(memberId);
	}

	public void sendToMember(Long memberId, String title, String body) {

		Optional<String> optionalToken = tokenRepository.find(memberId);

		String registrationToken = null;

		if (optionalToken.isPresent()) {
			registrationToken = optionalToken.get();
			log.info("saved registration Token = {}",registrationToken);
		} else {
			return;
		}

		Message message = Message.builder()
			.setToken(registrationToken)
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.build();

		FirebaseMessaging.getInstance().sendAsync(message);

	}

}

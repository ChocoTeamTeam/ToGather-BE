package chocoteamteam.togather.component.fcm;

import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.FCMException;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.Message;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FCMInitializer {

	@Value("${fcm.key.path}")
	private String fcmCredential;

	@Value("${fcm.key.scopes}")
	private List<String> fcmScopes;

	@PostConstruct
	public void init() {

		ClassPathResource resource = new ClassPathResource(fcmCredential);

		try (InputStream is = resource.getInputStream()) {
			GoogleCredentials credentials = GoogleCredentials
				.fromStream(is)
				.createScoped(fcmScopes);

			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(credentials)
				.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				log.info("Firebase application has been initialized");
			}

		} catch (IOException e) {
			log.error(e.getMessage());
			throw new FCMException(ErrorCode.FCM_INITIALIZATION_FAILED,e);
		}
	}
}

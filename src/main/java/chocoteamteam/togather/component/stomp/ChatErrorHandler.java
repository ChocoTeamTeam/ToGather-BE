package chocoteamteam.togather.component.stomp;

import chocoteamteam.togather.dto.ErrorResponse;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.TokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatErrorHandler extends StompSubProtocolErrorHandler {

	private final ObjectMapper objectMapper;

	@Override
	public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage,
		Throwable ex) {

		if (ex.getCause() instanceof TokenException) {
			TokenException exception = (TokenException) ex.getCause();

			return prepareErrorMessage(exception.getErrorCode());

		}

		return super.handleClientMessageProcessingError(clientMessage, ex);
	}

	private Message<byte[]> prepareErrorMessage(ErrorCode errorCode) {

		ErrorResponse response = ErrorResponse.builder()
			.errorMessage(errorCode.getErrorMessage())
			.errorCode(errorCode)
			.status(errorCode.getHttpStatus().value())
			.build();

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

		accessor.setMessage(errorCode.name());
		accessor.setLeaveMutable(true);

		try {
			return MessageBuilder.createMessage(objectMapper.writeValueAsString(response).getBytes(),
				accessor.getMessageHeaders());
		} catch (JsonProcessingException e) {
			return MessageBuilder.createMessage(errorCode.name().getBytes(),
				accessor.getMessageHeaders());
		}
	}
}

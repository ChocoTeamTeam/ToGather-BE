package chocoteamteam.togather.component.stomp;

import chocoteamteam.togather.dto.TokenMemberInfo;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.TokenException;
import chocoteamteam.togather.service.JwtService;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
public class StompJwtHandler implements ChannelInterceptor {

	private final JwtService jwtService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
			StompHeaderAccessor.class);

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			try {
				TokenMemberInfo info = jwtService.parseAccessToken(
					accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION).substring(7));

				log.info("Member connected WebSocket. id : {} , name : {}",info.getId(),info.getNickname());

				accessor.setUser(new UserPrincipal(String.valueOf(info.getId())));
			} catch (Exception e) {
				throw new TokenException(ErrorCode.INVALID_TOKEN,e);
			}
		}

		return message;
	}
}
